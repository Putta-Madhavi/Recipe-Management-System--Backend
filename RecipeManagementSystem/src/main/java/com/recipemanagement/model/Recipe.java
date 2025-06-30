package com.recipemanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List; // Keep this for List<Review>

@Entity
@Data
@Table(name = "recipies") 
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String cuisine;
    private String mealType;
    private int cookingTime;
    private String category;

    // This is for storing ingredients as a single string
    @Column(length = 1000) 
    private String ingredients;

    @Column(length = 2000)
    private String instructions;

    private String nutritionalInfo;
    private Double averageRating;
    private String imageUrl;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("recipe")
    private List<Review> reviews;
}