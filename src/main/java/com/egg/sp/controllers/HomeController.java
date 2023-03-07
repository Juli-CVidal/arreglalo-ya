package com.egg.sp.controllers;


import com.egg.sp.entities.Users;
import com.egg.sp.enums.Rol;
import com.egg.sp.exceptions.ServicesException;
import com.egg.sp.services.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private UsersService usersService;


    @GetMapping
    public String getIndex(ModelMap model) {
        List<Users> supplierList = usersService.findAllByRol(Rol.SUPPLIER);
        supplierList.sort(Comparator.comparing(Users::getName));
        model.put("suppliers", supplierList);
        return "index.html";
    }

    @GetMapping("/login")
    public String getLoginForm() {
        return "login.html";
    }

    @GetMapping("/signup/user")
    public String getFormUser(ModelMap model) {
        model.put("users", new Users());
        return "new-user.html";
    }

    @PostMapping("/signup/user")
    public String signUpUser(@Valid Users user, ModelMap model, BindingResult result) {
        if (result.hasErrors()) {
            model.put("users", user);
            model.put("errors", result.getAllErrors());
            return "new-user.html";
        }

        return createAccount(user, Rol.CUSTOMER, model);
    }


    @GetMapping("/signup/supplier")
    public String getFormSupplier(ModelMap model) {
        model.put("rol", "supplier");
        model.put("users", new Users());
        return "new-user.html";
    }

    @PostMapping("/signup/supplier")
    public String signUpSupplier(@Valid Users supplier, ModelMap model, BindingResult result) {
        if (result.hasErrors()) {
            model.put("users", supplier);
            model.put("errors", result.getAllErrors());
            return "new-user.html";
        }

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
        return "redirect:/";
    }

}
