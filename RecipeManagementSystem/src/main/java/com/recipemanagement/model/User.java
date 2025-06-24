package com.recipemanagement.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;

    @ElementCollection
    private List<String> dietaryPreferences;

    @ElementCollection
    private List<String> allergies;

    @ElementCollection
    private List<String> avoidIngredients;

    private String cookingSkillLevel;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews;
}
