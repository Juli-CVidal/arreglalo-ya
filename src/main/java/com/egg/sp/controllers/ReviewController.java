package com.egg.sp.controllers;


import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.egg.sp.entities.Review;
import com.egg.sp.entities.Users;
import com.egg.sp.exceptions.ServicesException;
import com.egg.sp.services.ReviewService;

@Controller
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/create/{supplierId}")
    public String create(@Valid Review review, @PathVariable("supplierId") Integer supplierId, ModelMap model, HttpSession session) {

        Users user = (Users) session.getAttribute("usserSesion");
        try {
            reviewService.create(review, user, supplierId);
            model.put("success", "¡Reseña añadida correctamente!");
        } catch (ServicesException se) {
            model.put("error", se.getMessage());
            model.put("review", review);
            return "profile";
        }
        return "redirect:/supplier/" + supplierId;
    }


    @PostMapping("/censure/{id}")
    public String censure(@PathVariable("id") Integer id, Integer supplierId, ModelMap model) {
        try {
            reviewService.censure(id);
            model.put("success","La review se ha censurado con éxito");
        } catch (ServicesException se) {
            model.put("error", se.getMessage());
        }
        return "redirect:/user/" + supplierId;
    }

    @PostMapping("/delete/{id}")
    public String delete(Integer id, ModelMap model) {

        try {
            reviewService.delete(id);
            model.put("success", "¡Reseña eliminada correctamente!");
        } catch (ServicesException se) {
            model.put("error", se.getMessage());
        }
        return "redirect:/review";
    }

}
