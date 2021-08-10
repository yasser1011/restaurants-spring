package com.example.backend.service;

import com.example.backend.models.Review;
import com.example.backend.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository){
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getReviewsOfRestaurant(Long restaurantId){
        return reviewRepository.findByRestaurantId(restaurantId);
    }
}
