package com.egg.sp.controllers;


import com.egg.sp.entities.Users;
import com.egg.sp.entities.Work;
import com.egg.sp.enums.Rol;
import com.egg.sp.exceptions.ServicesException;
import com.egg.sp.services.ReviewService;
import com.egg.sp.services.UsersService;
import com.egg.sp.services.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private WorkService workService;

    @Autowired
    private ReviewService reviewService;


    /**
     * This method is going to be used when an account wants to see his proper profile
     * @param session the current session (Here we obtain the account's info)
     * @param model
     * @return the profile document
     */
    @GetMapping()
    public String getProfile(HttpSession session, ModelMap model) {
        Users user = (Users) session.getAttribute("usserSesion");
        if (user.getRol() == Rol.SUPPLIER){
            addSupplierFields(model, user.getId());
        }
        model.put("users", user);
        return "profile";
    }

    /**
     * This method is going to be used when an account wants to see another profile
     * @param id    - the other account's id
     * @param model
     * @return the profile document
     */
    @GetMapping("/{id}")
    public String getProfile(HttpSession session,@PathVariable("id") Integer id, ModelMap model) {

        try {
            Users user = usersService.findById(id);
            model.put("user", user);
            if (user.getRol() == Rol.SUPPLIER){
                //The login id is that of the customer, and the one received as parameter is that of the supplier.
                Users customer = (Users) session.getAttribute("userSession");
                model.put("history",workService.findWorksHistory(customer.getId(), id));
            }
        } catch (ServicesException se) {
            model.put("error", se.getMessage());
            return "index";
        }
        return "profile";
    }

    @GetMapping("/suppliers")
    public String getAllSuppliers(ModelMap model) {
        model.put("supplierList", usersService.findAllByRol(Rol.SUPPLIER));
        return "suppliers-view";
    }


    /**
     * This method is going to be used
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/update/{id}")
    public String getForm(@PathVariable("id") Integer id, ModelMap model) {
        try {
            Users user = usersService.findById(id);
            model.put("users", user);
            return "profile-form";
        } catch (ServicesException se) {
            model.put("error", se.getMessage());
            return "index.html";
        }
    }

    @PostMapping("/{id}")
    public String updateProfile(@ModelAttribute("users") Users user, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.put("errors", result.getAllErrors());
            model.put("users", user);
            return "profile-form";
        }

        try {
            usersService.update(user);
        } catch (ServicesException se) {
            model.put("error", se.getMessage());
            model.put("users", user);
            return "profile.form";
        }
        model.put("success", "El perfil ha sido actualizado!");
        return "redirect:/user";
    }

    private void addSupplierFields(ModelMap model, Integer id){
        model.put("numberCustomers", workService.countDistinctCustomers(id));

        //this is the average score
        model.put("averageRating", reviewService.averageRating(id));

        List<Work> workRequest = workService.getFromSupplierJobOffer(id);
        List<Work> activeWork = workService.getFromSupplierAcceptWork(id);
        List<Work> workCompleted = workService.getFromSupplierCompletedWork(id);

        model.put("workRequest", workRequest);
        model.put("activeWork", activeWork);
        model.put("workCompleted", workCompleted);
    }
}