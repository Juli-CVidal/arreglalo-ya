package com.egg.sp.controllers;


import com.egg.sp.entities.Supplier;
import com.egg.sp.entities.Users;
import com.egg.sp.exceptions.ServicesException;
import com.egg.sp.services.SupplierService;
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
    
    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public String getIndex(ModelMap model) {
        List<Supplier> supplierList = supplierService.getAll();
        supplierList.sort(Comparator.comparing(Users::getName));
        model.put("suppliers", supplierList);
        return "index.html";
    }

    @GetMapping("/login")
    public String getLoginForm(){
        return "login.html";
    }

    @GetMapping("/signup/user")
    public String getFormUser(ModelMap model) {
        model.put("user", new Users());
        return "new-user";
    }

    @PostMapping("/signup/user")
    public String signUpUser(@Valid Users user, ModelMap model, BindingResult result){
        if (result.hasErrors()){
            model.put("user", user);
            model.put("errors", result.getAllErrors());
            return "new-user";
        }

        try {
            usersService.create(user);
        } catch (ServicesException se){
            model.put("user", user);
            model.put("error",se.getMessage());
            return "new-user";
        }

        model.put("success", "su cuenta ha sido creada exitosamente!");
        //To the account profile
        return "redirect:/user";
    }


    @GetMapping("/signup/supplier")
    public String getFormSupplier(ModelMap model){
        model.put("supplier", new Supplier());
        return "new-supplier";
    }

    @PostMapping("/signup/supplier")
    public String signUpSupplier(@Valid Supplier supplier, ModelMap model, BindingResult result){
        if (result.hasErrors()){
            model.put("user", supplier);
            model.put("errors", result.getAllErrors());
            return "new-supplier";
        }

        try {
            supplierService.create(supplier);
        } catch (ServicesException se){
            model.put("supplier", supplier);
            model.put("error",se.getMessage());
            return "new-supplier";
        }

        model.put("success", "su cuenta ha sido creada exitosamente!");
        //To the account profile
        return "redirect:/user";
    }

}
