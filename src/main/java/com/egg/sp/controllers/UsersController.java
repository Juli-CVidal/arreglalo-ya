package com.egg.sp.controllers;


import com.egg.sp.entities.Supplier;
import com.egg.sp.entities.Users;
import com.egg.sp.exceptions.ServicesException;
import com.egg.sp.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UsersController {

    @Autowired
    private UsersService usersService;


    /**
     * This method is going to be used when an account wants to see his proper profile
     * @param session the current session (Here we obtain the account's info)
     * @param model
     * @return the profile document
     */
    @GetMapping()
    public String getProfile(HttpSession session, ModelMap model) {
        Users user = (Users) session.getAttribute("usserSesion");
        if (user instanceof Supplier){
            model.put("supplier",(Supplier) user);
            return "profile";
        }
        model.put("user", user);
        return "profile";
    }

    /**
     * This method is going to be used when an account wants to see another profile
     * @param id    - the other account's id
     * @param model
     * @return the profile document
     */
    @GetMapping("/{id}")
    public String getProfile(@PathVariable("id") Integer id, ModelMap model) {

        try {
            Users user = usersService.findById(id);
            model.put("user", user);
        } catch (ServicesException se) {
            model.put("error", se.getMessage());
            return "index";
        }
        return "profile";
    }

    @GetMapping("/all")
    public String getAllUsers(ModelMap model) {
        model.put("usersList", usersService.getAll());
        return "all-users";
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
}