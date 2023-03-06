package com.egg.sp.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.egg.sp.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egg.sp.entities.Review;
import com.egg.sp.entities.Users;
import com.egg.sp.exceptions.ServicesException;
import com.egg.sp.repositories.ReviewRepository;
import com.egg.sp.repositories.WorkRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private WorkRepository workRepository;

    @Autowired
    private UsersService usersService;

    @Transactional
    public void create(Review review, Users user, Integer supplierId) throws ServicesException {
        review.setUser(user);
        review.setSupplier(usersService.findSupplierById(supplierId));
        review.setCreationDate(new Date(System.currentTimeMillis()));
        reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public List<Review> getAll() {
        return reviewRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Review> orderByScore() {
        return reviewRepository.orderByScore();
    }

    @Transactional(readOnly = true)
    public Integer countContractedTimes(Integer userId, Integer supplierId) {
        return workRepository.countContractedTimes(userId, supplierId);
    }

    @Transactional(readOnly = true)
    public Review findById(Integer id) throws ServicesException {
        return getFromOptional(reviewRepository.findById(id));
    }

    @Transactional
    public void censure(Integer id) throws ServicesException {
        Review review = findById(id);
        review.setContent("CENSURADO");
        reviewRepository.save(review);
    }

    @Transactional
    public void delete(Integer id) throws ServicesException {
        Review review = findById(id);
        reviewRepository.delete(review);

    }

    @Transactional(readOnly = true)
    public double averageRating(Integer idSupplier) {
        List<Review> reviews = reviewRepository.getFromSupplier(idSupplier);
        return reviews.stream().mapToDouble(review -> review.getScore()).average().orElse(0d);
    }

    private Review getFromOptional(Optional<Review> reviewOpt) throws ServicesException {
        if (reviewOpt.isEmpty()) {
            throw new ServicesException("No Review found");
        }
        return reviewOpt.get();
    }
}
