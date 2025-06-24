package com.recipemanagement.controller;

import com.recipemanagement.model.User;
import com.recipemanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Registration
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    // Login
    @PostMapping("/login")
    public String loginUser(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        return userService.loginUser(email, password);
    }

    // Optional: Get all users (for admin/testing)
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
