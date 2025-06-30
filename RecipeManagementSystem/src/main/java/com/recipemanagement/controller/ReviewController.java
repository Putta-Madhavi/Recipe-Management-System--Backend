package com.recipemanagement.controller;

import com.recipemanagement.model.Recipe;
import com.recipemanagement.model.Review;
import com.recipemanagement.model.User;
import com.recipemanagement.repository.RecipeRepository;
import com.recipemanagement.repository.UserRepository;
import com.recipemanagement.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @PostMapping
    public Review createReview(@RequestBody Review review,
                               @RequestParam Long userId,
                               @RequestParam Long recipeId) {
        User user = userRepository.findById(userId).orElse(null);
        Recipe recipe = recipeRepository.findById(recipeId).orElse(null);

        if (user == null || recipe == null) {
            throw new RuntimeException("User or Recipe not found");
        }

        review.setUser(user);
        review.setRecipe(recipe);

        return reviewService.saveReview(review);
    }

    @GetMapping
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/recipe/{recipeId}")
    public List<Review> getReviewsByRecipe(@PathVariable Long recipeId) {
        return reviewService.getReviewsByRecipe(recipeId);
    }

    @GetMapping("/recipe/{recipeId}/average-rating")
    public Double getAverageRating(@PathVariable Long recipeId) {
        return reviewService.getAverageRatingByRecipeId(recipeId);
    }
}
