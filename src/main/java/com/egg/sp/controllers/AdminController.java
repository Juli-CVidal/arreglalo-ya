package com.egg.sp.controllers;

import com.egg.sp.entities.Users;
import com.egg.sp.exceptions.ServicesException;
import com.egg.sp.services.ProfessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.egg.sp.entities.Profession;
import com.egg.sp.services.UsersService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
	
	 @Autowired
	    private UsersService usersService;

	 @Autowired
	 private ProfessionService professionService;


	@PostMapping("/new-profession")
	public String newProfession(Profession profession, ModelMap model) {

		try {
			professionService.create(profession);
		} catch (ServicesException se) {
			model.put("error", se.getMessage());
			return "panel.html";
		}
		model.put("success", "Se creo la nueva profesion");
		return "redirect:/admin/dashboard";
	}

	@GetMapping("/dashboard")
	public String adminPanel(ModelMap model, HttpSession session) {
		model.put("usersList", usersService.getAll());
		Users user = (Users) session.getAttribute("userSession");
		model.put("profession", new Profession());
		model.put("user",user);
		return "panel.html";
	}


}
