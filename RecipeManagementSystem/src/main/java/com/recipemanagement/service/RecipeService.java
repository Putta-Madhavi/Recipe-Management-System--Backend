package com.recipemanagement.service;

import com.recipemanagement.model.Recipe;
import com.recipemanagement.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    // ✅ Cache all recipes
    @Cacheable("recipes")
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    // ✅ Cache search results based on filters
    @Cacheable(value = "searchResults", key = "#cuisine + '-' + #mealType + '-' + #maxTime + '-' + #diet + '-' + #ingredient")
    public List<Recipe> searchRecipes(String cuisine, String mealType, Integer maxTime, String diet, String ingredient) {
        return recipeRepository.searchRecipes(cuisine, mealType, maxTime, diet, ingredient);
    }
}
