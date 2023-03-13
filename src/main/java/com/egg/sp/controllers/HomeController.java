package com.egg.sp.controllers;

import com.egg.sp.entities.Users;
import com.egg.sp.enums.Rol;
import com.egg.sp.exceptions.ServicesException;
import com.egg.sp.services.ProfessionService;
import com.egg.sp.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private ProfessionService professionService;

    @GetMapping
    public String getIndex(HttpSession session, ModelMap model) {
        Users user = (Users) session.getAttribute("userSession");
        if (null != user && user.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        model.put("logged", user != null);
        List<Users> supplierList = usersService.findAllByRol(Rol.SUPPLIER);
        supplierList.stream()
        .filter(Objects::nonNull)
        .sorted(Comparator.comparingDouble(Users::getGeneralScore))
        .collect(Collectors.toList());

        model.put("suppliers", supplierList);

        model.put("professions", professionService.findAll());
        return "index.html";
    }

    @GetMapping("/signup-select")
    public String getSelectPage() {
        return "signup-select.html";
    }

    @GetMapping("/login")
    public String getLoginForm(@RequestParam(required = false) String error, ModelMap model) {

        if (error != null) {
            model.put("error", "Usuario o contraseña invalida");
        }
        return "login.html";
    }

    @GetMapping("/signup/user")
    public String getFormUser(ModelMap model) {
        model.put("users", new Users());
        return "new-user.html";
    }

    @PostMapping("/signup/user")
    public String signUpUser(@Valid Users user, @RequestParam(value = "imageFile", required = false) MultipartFile image, ModelMap model, BindingResult result) throws IOException {
        if (result.hasErrors()) {
            model.put("users", user);
            model.put("errors", result.getAllErrors());
            return "new-user.html";
        }
        ConvertImageToString(user, image);
        return createAccount(user, Rol.CUSTOMER, model);
    }

    @GetMapping("/signup/supplier")
    public String getFormSupplier(ModelMap model) {
        model.put("professions", professionService.findAll());
        model.put("users", new Users());
        return "new-supplier.html";
    }

    @PostMapping("/signup/supplier")
    public String signUpSupplier(@Valid Users supplier, @RequestParam(value = "imageFile", required = false) MultipartFile image, ModelMap model, BindingResult result) throws IOException {

        if (result.hasErrors()) {
            model.put("users", supplier);
            model.put("errors", result.getAllErrors());
            return "new-user.html";
        }
        ConvertImageToString(supplier, image);
        return createAccount(supplier, Rol.SUPPLIER, model);
    }

    private String createAccount(Users user, Rol accountType, ModelMap model) {
        try {
            user.setRol(accountType);
            usersService.create(user);
        } catch (ServicesException se) {
            model.put("users", user);
            model.put("error", se.getMessage());
            return "new-user.html";
        }

        model.put("success", "su cuenta ha sido creada exitosamente!");
        //To the account profile

        return "redirect:/login";
    }

    private void ConvertImageToString(Users user, MultipartFile image) throws IOException {
        byte[] imageBytes = image.getBytes();
        String Image = Base64.getEncoder().encodeToString(imageBytes);
        user.setImage(Image);
    }

    @GetMapping("/complaint/{id}")
    public String complaint(@PathVariable("id") Integer id, HttpSession session, ModelMap model) {

        Users user = (Users) session.getAttribute("userSession");
        model.put("user", user);

        model.put("supplierId", id);
        return "complaint-form.html";
    }
}
