package com.recipemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching  // ✅ Enable Spring Caching
public class RecipeManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipeManagementSystemApplication.class, args);
	}
}
