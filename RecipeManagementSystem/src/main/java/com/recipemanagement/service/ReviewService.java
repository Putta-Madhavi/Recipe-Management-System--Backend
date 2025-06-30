package com.recipemanagement.service;

import com.recipemanagement.model.Review;
import com.recipemanagement.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getReviewsByRecipe(Long recipeId) {
        return reviewRepository.findByRecipeId(recipeId);
    }

    public Double getAverageRatingByRecipeId(Long recipeId) {
        return reviewRepository.getAverageRatingByRecipeId(recipeId);
    }
}
