package com.recipemanagement.repository;

import com.recipemanagement.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByRecipeId(Long recipeId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.recipe.id = :recipeId")
    Double getAverageRatingByRecipeId(@Param("recipeId") Long recipeId);
}
