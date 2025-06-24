package com.recipemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipemanagement.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
