package com.recipemanagement.repository;

import com.recipemanagement.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("SELECT DISTINCT r FROM Recipe r " +
           "WHERE (:cuisine IS NULL OR LOWER(r.cuisine) = LOWER(:cuisine)) " + 
           "AND (:mealType IS NULL OR LOWER(r.mealType) = LOWER(:mealType)) " + 
           "AND (:maxTime IS NULL OR r.cookingTime <= :maxTime) " +
           "AND (:diet IS NULL OR LOWER(r.nutritionalInfo) LIKE LOWER(CONCAT('%', :diet, '%'))) " + 
           "AND (:ingredient IS NULL OR LOWER(r.ingredients) LIKE LOWER(CONCAT('%', :ingredient, '%')))") 
    List<Recipe> searchRecipes(
            @Param("cuisine") String cuisine,
            @Param("mealType") String mealType,
            @Param("maxTime") Integer maxTime,
            @Param("diet") String diet,
            @Param("ingredient") String ingredient
    );

    List<Recipe> findByCategoryIgnoreCase(String category);
}