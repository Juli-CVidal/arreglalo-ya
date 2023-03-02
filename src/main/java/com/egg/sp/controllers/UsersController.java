package com.egg.sp.controllers;


import com.egg.sp.entities.Users;
import com.egg.sp.exceptions.ServicesException;
import com.egg.sp.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping("/{id}")
    public String getProfile(@PathVariable("id") Integer id, ModelMap model){

        try{
            Users user = usersService.findById(id);
            model.put("user", user);
        } catch(ServicesException se){
            model.put("error", se.getMessage());
            return "index";
        }
        return "profile";
    }

    @GetMapping("/all")
    public String getAllUsers(ModelMap model){
        model.put("usersList", usersService.getAll());
        return "all-users";
    }


    @GetMapping("/update/{id}")
    public String getForm(@PathVariable("id") Integer id, ModelMap model){
        try{
            Users user = usersService.findById(id);
            model.put("users",user);
            return "profile-form";
        }catch (ServicesException se){
            model.put("error",se.getMessage());
            return "index.html";
        }
    }

    @PostMapping("/{id}")
    public String updateProfile(@ModelAttribute("users") Users user, BindingResult result, ModelMap model){
        if (result.hasErrors()){
            model.put("errors",result.getAllErrors());
            model.put("users",user);
            return "profile";
        }

        try {
            usersService.update(user);
        }catch(ServicesException se){
            model.put("error",se.getMessage());
            return "profile";
        }
        model.put("success", "El perfil ha sido actualizado!");
        return "redirect:/" + user.getId();
    }
}
