package com.egg.sp.controllers;


import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.egg.sp.entities.Review;
import com.egg.sp.entities.Users;
import com.egg.sp.exceptions.ServicesException;
import com.egg.sp.services.ReviewService;
import java.io.IOException;
import java.util.Base64;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/create/{supplierId}")
    public String getForm(@PathVariable("supplierId") Integer supplierId, HttpSession session, ModelMap model) {
        model.put("supplierId", supplierId);
        model.put("user", (Users) session.getAttribute("userSession"));
        model.put("review", new Review());
        return "review-form.html";
    }

    @PostMapping("/create")
    public String create(@Valid Review review, Integer supplierId, @RequestParam(value="imageFile",required=false) MultipartFile image, ModelMap model, HttpSession session) throws IOException {
        Users user = (Users) session.getAttribute("userSession");
        try {
            ConvertImageToString(review, image);
            review.setUser(user);
            reviewService.create(review, supplierId);
            model.put("success", "¡Reseña añadida correctamente!");
        } catch (ServicesException se) {
            System.out.println(se.getMessage());
            model.put("error", se.getMessage());
            model.put("user",  user);
            model.put("review", review);
            return "review-form.html";
        }
        return "redirect:/user/" + supplierId;
    }


    @PostMapping("/censure")
    public String censure(@RequestParam("reviewId") Integer reviewId,
                          @RequestParam("supplierId") Integer supplierId,
                          ModelMap model) {
        try {
            reviewService.censure(reviewId);
            model.put("success", "La review se ha censurado con éxito");
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
    
    private void ConvertImageToString(Review review, MultipartFile image) throws IOException {
        byte[] imageBytes = image.getBytes();
        String Image = Base64.getEncoder().encodeToString(imageBytes);
        review.setImage(Image);
    }

}