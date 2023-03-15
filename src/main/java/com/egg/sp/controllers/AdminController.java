package com.egg.sp.controllers;

import com.egg.sp.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.egg.sp.enums.Rol;
import com.egg.sp.services.UsersService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
	
	 @Autowired
	    private UsersService usersService;
	
	@GetMapping("/dashboard")
	public String adminPanel(ModelMap model, HttpSession session) {
		model.put("usersList", usersService.getAll());
		Users user = (Users) session.getAttribute("userSession");
		model.put("user",user);
		return "panel.html";
	}


}
