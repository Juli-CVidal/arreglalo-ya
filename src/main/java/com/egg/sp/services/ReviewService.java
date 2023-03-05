package com.egg.sp.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    private SupplierService supplierService;

    @Transactional
    public void create(Review review, Users user, Integer supplierId) throws ServicesException {
        
        validateScore(review);
        
        review.setUser(user);
        review.setSupplier(supplierService.findById(supplierId));
        
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
    public Review getById(Integer id) throws ServicesException {
        Optional<Review> reviewOpt = reviewRepository.findById(id);
        if (reviewOpt.isEmpty()) {
            throw new ServicesException("No Review found");
        }
        return reviewOpt.get();
    }

    @Transactional
    public void delete(Integer id) throws ServicesException {
        Review review = getById(id);
        reviewRepository.delete(review);

    }

    @Transactional(readOnly = true)
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

    /*@Transactional(readOnly = true)
    public void validateContracting(Integer userId, Integer supplierId) throws ServicesException {

        if (workRepository.countContractedTimes(userId, supplierId) == 0) {
            throw new ServicesException("Debes haber contratado este servicio para poder dejar tu reseña");
        }

    }*/
}
