package com.egg.sp.controllers;

import com.egg.sp.entities.Users;
import com.egg.sp.entities.Work;
import com.egg.sp.enums.Acceptance;
import com.egg.sp.exceptions.ServicesException;
import com.egg.sp.services.UsersService;
import com.egg.sp.services.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/work")
public class WorkController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private WorkService workService;

    @PostMapping("/create")
    //A user can ONLY make a service request from the supplier's profile.
    //Both the userId and the supplierId can be obtained from that view, and loaded to work
    public String create(@Valid Work work, HttpSession session, Integer supplierId, ModelMap model, BindingResult result) {
        if (result.hasErrors()) {
            model.put("errors", result.getAllErrors());
            model.put("work", work);
            model.put("users", work.getSupplier());
            return "profile";
        }
        try {
            work.setUser((Users) session.getAttribute("userSession"));
            workService.create(work, usersService.findSupplierById(supplierId));
        } catch (ServicesException se) {
            model.put("error", se.getMessage());
            model.put("work", work);
            model.put("users", work.getSupplier());
            return "profile";
        }

        model.put("success", "El trabajo se ha solicitado correctamente!");
        return "redirect:/user/" + supplierId;
    }

    @PostMapping("/refuse/{id}")
    public String refuse(@PathVariable("id") Integer id, ModelMap model) {
        return changeWorkState(id, Acceptance.RECHAZADO, model);
    }

    @PostMapping("/accept/{id}")
    public String accept(@PathVariable("id") Integer id,Double price, ModelMap model) {
        try {
            workService.acceptWork(id, price);
            model.put("success", "El trabajo ha sido aceptado con éxito!");
        } catch (ServicesException se) {
            model.put("error", se.getMessage());
        }
        return "redirect:/user";
    }

    @PostMapping("/complete/{id}")
    public String complete(@PathVariable("id") Integer id, ModelMap model) {
        return changeWorkState(id, Acceptance.FINALIZADO, model);
    }

    private String changeWorkState(Integer id, Acceptance state, ModelMap model) {
        try {
            workService.modifyState(id, state);
            model.put("success", "El trabajo ha sido " + state.toString().toLowerCase());
        } catch (ServicesException se) {
            model.put("error", se.getMessage());
        }
        return "redirect:/user";
    }
}