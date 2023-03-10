package com.egg.sp.controllers;

import com.egg.sp.entities.Users;
import com.egg.sp.entities.Work;
import com.egg.sp.enums.Acceptance;
import com.egg.sp.exceptions.ServicesException;
import com.egg.sp.services.ProfessionService;
import com.egg.sp.services.UsersService;
import com.egg.sp.services.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/work")
public class WorkController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private ProfessionService professionService;

    @Autowired
    private WorkService workService;

    @GetMapping("/create/{supplierId}")
    public String create(@PathVariable("supplierId") Integer supplierId,ModelMap model){
        model.put("supplierId",supplierId);
        model.put("work", new Work());
        model.put("professions", professionService.findAll());
        return "work-form.html";
    }

    @PostMapping("/create")
    //A user can ONLY make a service request from the supplier's profile.
    //Both the userId and the supplierId can be obtained from that view, and loaded to work
    public String create(@Valid Work work, @RequestParam("supplierId") Integer supplierId, HttpSession session, ModelMap model, BindingResult result) {
        if (result.hasErrors()) {
            model.put("errors", result.getAllErrors());
            model.put("professions", professionService.findAll());
            model.put("work", work);
            return "work-form.html";
        }
        try {
            work.setUser((Users) session.getAttribute("userSession"));
            work.setSupplier(usersService.findSupplierById(supplierId));
            workService.create(work);
        } catch (ServicesException se) {
            model.put("error", se.getMessage());
            model.put("professions", professionService.findAll());
            model.put("work", work);
            return "work-form.html";
        }

        model.put("success", "El trabajo se ha solicitado correctamente!");
        return "redirect:/user/" + supplierId;
    }

    @PostMapping("/refuse")
    public String refuse(@RequestParam("workId") Integer workId, ModelMap model) {
        return changeWorkState(workId, Acceptance.RECHAZADO, model);
    }

    @PostMapping("/accept")
    public String accept(@RequestParam("workId") Integer workId, @RequestParam("price") Double price, ModelMap model) {
        try {
            workService.acceptWork(workId, price);
            model.put("success", "El trabajo ha sido aceptado con éxito!");
        } catch (ServicesException se) {
            model.put("error", se.getMessage());
        }
        return "redirect:/user";
    }

    @PostMapping("/complete")
    public String complete(@RequestParam("workId") Integer workId, ModelMap model) {
        return changeWorkState(workId, Acceptance.FINALIZADO, model);
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