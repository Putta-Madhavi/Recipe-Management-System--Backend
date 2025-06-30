package com.recipemanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rating;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"reviews"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    @JsonIgnoreProperties({"reviews"})
    private Recipe recipe;
}
