package com.egg.sp.controllers;

import com.egg.sp.entities.Review;
import com.egg.sp.entities.Users;
import com.egg.sp.entities.Work;
import com.egg.sp.enums.Acceptance;
import com.egg.sp.enums.Rol;
import com.egg.sp.exceptions.ServicesException;
import com.egg.sp.services.ProfessionService;
import com.egg.sp.services.ReviewService;
import com.egg.sp.services.UsersService;
import com.egg.sp.services.WorkService;
import java.io.IOException;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private WorkService workService;
    @Autowired
    private ProfessionService professionService;

    /**
     * This method is going to be used when an account wants to see his proper
     * profile
     *
     * @param session the current session (Here we obtain the account's info)
     * @param model
     * @return the profile document
     */
    @GetMapping()
    public String getProfile(HttpSession session, ModelMap model) {
        List<Review> reviewList;
        List<Work> workList;
        Users user = (Users) session.getAttribute("userSession");

        model.put("profile", user);
        model.put("userId", user.getId());
        if (user.getRol() == Rol.SUPPLIER) {
            reviewList = reviewService.getBySupplier(user.getId());
            workList = workService.getWorksSupplier(user.getId());
            System.out.println(workList);
        } else {
            reviewList = reviewService.getByUser(user.getId());
            workList = workService.getWorksUser(user.getId());
        }
        String image = user.getImage();

        model.put("image", image);
        model.put("reviews", reviewList);
        model.put("works", workList);
        return "profile";
    }

    /**
     * This method is going to be used when an account wants to see another
     * profile
     *
     * @param id - the other account's id
     * @param model
     * @return the profile document
     */
    @GetMapping("/{id}")
    public String getProfile(HttpSession session, @PathVariable("id") Integer id, ModelMap model) {
        //The login id is that of the customer, and the one received as parameter is that of the supplier.
        Users customer = (Users) session.getAttribute("userSession");

        if (customer.getId() == id) {
            return "redirect:/user";
        }
        try {
            model.put("userId", customer.getId());
            Users user = usersService.findById(id);
            if (user.getRol() != Rol.SUPPLIER) {
                throw new ServicesException("Sólo se pueden ver perfiles de proveedores");
            }
            String image = customer.getImage();

            model.put("profile", user);
            model.put("image", image);
            model.put("reviews", reviewService.getBySupplier(id));
            List<Work> worksList =  workService.findWorksHistory(customer.getId(), id);
            model.put("has_accepted_work", worksList.stream().anyMatch(work -> work.getAcceptance() == Acceptance.ACEPTADO));
            model.put("works", worksList);
        } catch (ServicesException se) {
            model.put("error", se.getMessage());
            return "index";
        }
        return "profile";
    }

    @GetMapping("/suppliers")
    public String getAllSuppliers(ModelMap model, HttpSession session) {
        Users user = (Users) session.getAttribute("userSession");
        if (user != null){
            model.put("user",user);
        }
    	model.put("supplierList", usersService.findAllByRol(Rol.SUPPLIER));
        model.put("logged", user != null);
    	model.put("professions", professionService.findAll());
        return "suppliers-view";
    }


    @GetMapping("/update/{id}")
    public String getForm(@PathVariable("id") Integer id, ModelMap model) {
        try {
            Users user = usersService.findById(id);
            model.put("user", user);
            model.put("professions", professionService.findAll());
            return "profile-form.html";
        } catch (ServicesException se) {
            model.put("error", se.getMessage());
            return "index.html";
        }
    }

    @PostMapping()
    public String updateProfile(@ModelAttribute("user") Users user, @RequestParam(value = "imageFile", required = false) MultipartFile image,
            @RequestParam(value="confirm") String confirm, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.put("errors", result.getAllErrors());
            model.put("users", user);
            return "profile-form";
        }

        try {
            if (null != image){
                setImage(user,image);
            }

            user.setGeneralScore(reviewService.getGeneralScore(user.getId()));
            usersService.update(user, confirm);
        } catch (ServicesException | IOException se) {
            model.put("error", se.getMessage());
            model.put("professions", professionService.findAll());
            model.put("users", user);
            return "profile-form";
        }
        model.put("success", "El perfil ha sido actualizado!");
        return "redirect:/user";
    }

    @PostMapping("/become-supplier")
    public String becomeSupplier(@RequestParam("id") Integer id, ModelMap model){
        try {
            Users user = usersService.findById(id);
            usersService.becomeSupplier(user);
            model.put("user", user);
            model.put("professions", professionService.findAll());
            return "profile-form.html";
        } catch (ServicesException se) {
            model.put("error", se.getMessage());
            return "/user/" + id;
        }
    }

    private void setImage(Users user, MultipartFile image) throws IOException {
        byte[] imageBytes = image.getBytes();
        String Image = Base64.getEncoder().encodeToString(imageBytes);
        user.setImage(Image);
    }
}
