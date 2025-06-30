// RECIPE CONTROLLER - YOU MUST CHANGE THIS PART
package com.recipemanagement.controller;

import com.recipemanagement.model.Recipe;
import com.recipemanagement.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*; // Ensure @RequestBody is imported

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

   
    @PostMapping 
    public Recipe createRecipe(@RequestBody Recipe recipe) {
      
        System.out.println("Received recipe from frontend: " + recipe.getName());
        return recipeService.saveRecipe(recipe);
    }
 


    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/{id}")
    public Recipe getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipeById(id);
    }

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

    @GetMapping("/top")
    public List<Recipe> getTopRatedRecipes(@RequestParam(defaultValue = "3") int count) {
        return recipeService.getTopRatedRecipes(count);
    }

    @GetMapping("/category")
    public List<Recipe> getRecipesByCategory(@RequestParam String category) {
        return recipeService.getRecipesByCategory(category);
    }
}