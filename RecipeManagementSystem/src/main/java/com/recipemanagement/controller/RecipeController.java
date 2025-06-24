package com.recipemanagement.controller;

import com.recipemanagement.model.Recipe;
import com.recipemanagement.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @PostMapping
    public Recipe createRecipe(@RequestBody Recipe recipe) {
        return recipeService.saveRecipe(recipe);
    }

    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    // ✅ Full Search with cuisine, mealType, time, dietary, ingredients
    @GetMapping("/search")
    public List<Recipe> searchRecipes(
        @RequestParam(required = false) String cuisine,
        @RequestParam(required = false) String mealType,
        @RequestParam(required = false) Integer maxCookingTime,
        @RequestParam(required = false) String dietaryRequirement,
        @RequestParam(required = false) String ingredient
    ) {
        return recipeService.searchRecipes(cuisine, mealType, maxCookingTime, dietaryRequirement, ingredient);
    }
}
