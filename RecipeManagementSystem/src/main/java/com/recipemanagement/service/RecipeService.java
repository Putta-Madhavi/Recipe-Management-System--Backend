package com.recipemanagement.service;

import com.recipemanagement.model.Recipe;
import com.recipemanagement.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id).orElse(null);
    }

    public List<Recipe> searchRecipes(String cuisine, String mealType, Integer maxTime, String diet, String ingredient) {
       
    	// This line is correct as it calls the updated repository method
        return recipeRepository.searchRecipes(cuisine, mealType, maxTime, diet, ingredient);
    }

    public List<Recipe> getTopRatedRecipes(int count) {
        return recipeRepository.findAll().stream()
                .sorted(Comparator.comparingDouble(this::calculateAverageRating).reversed())
                .limit(count)
                .toList();
    }

    private double calculateAverageRating(Recipe recipe) {
        if (recipe.getReviews() == null || recipe.getReviews().isEmpty()) return 0;
        return recipe.getReviews().stream()
                .mapToInt(r -> r.getRating())
                .average()
                .orElse(0);
    }

    public List<Recipe> getRecipesByCategory(String category) {
        return recipeRepository.findByCategoryIgnoreCase(category);
    }
}