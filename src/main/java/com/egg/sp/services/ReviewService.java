package com.egg.sp.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egg.sp.entities.Review;
import com.egg.sp.exceptions.ServicesException;
import com.egg.sp.repositories.ReviewRepository;
import com.egg.sp.repositories.WorkRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private WorkRepository workRepository;

    public void create(Review review) throws ServicesException {
        validateScore(review);
        review.setCreationDate(new Date(System.currentTimeMillis()));
        reviewRepository.save(review);
    }

    public List<Review> getAll() {
        return reviewRepository.findAll();
    }

    public List<Review> orderByScore() {
        return reviewRepository.orderByScore();
    }

    public Integer countContractedTimes(Integer userId, Integer supplierId) {
        return workRepository.countContractedTimes(userId, supplierId);
    }

    public Review getById(Integer id) throws ServicesException {
        Optional<Review> reviewOpt = reviewRepository.findById(id);
        if (reviewOpt.isEmpty()) {
            throw new ServicesException("No Review found");
        }
        return reviewOpt.get();
    }

    public void update(Review review) throws ServicesException {
        if (null == review || null == review.getId()) {
            throw new ServicesException("Invalid review");
        }
        create(review);
    }

    public void delete(Integer id) throws ServicesException {
        Review review = getById(id);
        reviewRepository.delete(review);

    }

    public double averageRating(Integer idSupplier) {
        List<Review> reviews = reviewRepository.getFromSupplier(idSupplier);
        if (reviews.isEmpty()) {
            return 0;
        }
        double sum = 0;
        for (Review review : reviews) {
            sum += review.getScore();
        }
        return sum / reviews.size();
    }

    private void validateScore(Review review) throws ServicesException {

        if (null == review.getScore() || review.getScore() < 0 || review.getScore() > 5) {
            throw new ServicesException("Reiew inválida");
        }
    }

    public void validateContratacion(Integer userId, Integer supplierId) throws ServicesException {

        if (workRepository.countContractedTimes(userId, supplierId) == 0) {
            throw new ServicesException("Debes haber contratado este servicio para poder dejar tu reseña");
        }

    }
}
