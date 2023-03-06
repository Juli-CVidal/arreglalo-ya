package com.egg.sp.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/all")
    public String getAll(ModelMap model) {

        List<Review> reviews = reviewService.getAll();
        model.put("reviews", reviews);
        return "reviews-view";
    }

    @GetMapping("/{id}")
    public String getReview(@PathVariable("id") Integer id, ModelMap model) throws ServicesException {
        try {
            Review review = reviewService.getById(id);
            model.put("review", review);
        } catch (ServicesException se) {
            model.put("error", se.getMessage());
            return "index";
        }
        return "review-view";
    }

    @GetMapping("/save")
    public String getForm(ModelMap model) {

        return "review-form";
    }

    @PostMapping("/save")
    public String create(@Valid Review review, ModelMap model, BindingResult result, Users user, Integer supplierId) {

        if (result.hasErrors()) {
            model.put("errors", result.getAllErrors());
            model.put("review", review);
            return "redirect:/" + review.getSupplier().getId();
        }

        try {
            reviewService.create(review, user, supplierId);
            model.put("success", "review added successfully");
        } catch (ServicesException se) {
            model.put("error", se.getMessage());
            model.put("review", review);
            return "review-form";
        }

        return "redirect:/review";
    }

    @GetMapping("/modify/{id}")
    public String modify(Integer id, ModelMap model) {

        try {
            Review review = reviewService.getById(id);
            model.put("review", review);
        } catch (ServicesException se) {
            model.put("error", se.getMessage());
            return "reviews-view";
        }
        return "review-form";
    }

    @PostMapping("/delete/{id}")
    public String delete(Integer id, ModelMap model) {

        try {
            reviewService.delete(id);
            model.put("success", "review dissmissed successfully");
        } catch (ServicesException se) {
            model.put("error", se.getMessage());
        }
        return "redirect:/review";
    }

}
