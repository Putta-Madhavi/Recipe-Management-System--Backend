package com.recipemanagement.repository;

import com.recipemanagement.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("SELECT r FROM Recipe r " +
           "WHERE (:cuisine IS NULL OR r.cuisine = :cuisine) " +
           "AND (:mealType IS NULL OR r.mealType = :mealType) " +
           "AND (:maxTime IS NULL OR r.cookingTime <= :maxTime) " +
           "AND (:diet IS NULL OR LOWER(r.nutritionalInfo) LIKE LOWER(CONCAT('%', :diet, '%'))) " +
           "AND (:ingredient IS NULL OR EXISTS (SELECT i FROM r.ingredients i WHERE LOWER(i) LIKE LOWER(CONCAT('%', :ingredient, '%'))))")
    List<Recipe> searchRecipes(
        String cuisine,
        String mealType,
        Integer maxTime,
        String diet,
        String ingredient
    );
}
