package com.egg.sp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.egg.sp.enums.Rol;
import com.egg.sp.services.UsersService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	 @Autowired
	    private UsersService usersService;
	
	@GetMapping("/dashboard")
	public String adminPanel(ModelMap model) {
		model.put("usersList", usersService.getAll());
		return "panel.html";
	}


}
