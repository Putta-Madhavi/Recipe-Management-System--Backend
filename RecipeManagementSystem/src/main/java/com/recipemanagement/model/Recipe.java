package com.recipemanagement.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String cuisine;         // e.g., Indian, Italian
    private String mealType;        // e.g., breakfast, lunch, dinner
    private int cookingTime;        // in minutes

    @ElementCollection
    private List<String> ingredients;   // e.g., ["rice", "onion", "tomato"]

    @Column(length = 2000)
    private String instructions;

    private String nutritionalInfo;     // e.g., "vegetarian, low-carb, gluten-free"

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Review> reviews;
}
