package com.recipemanagement.service;

import com.recipemanagement.model.User;
import com.recipemanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String saveUser(User user) {
        
        Optional<User> nameExists = userRepository.findByName(user.getName());
        if (nameExists.isPresent()) {
            return "Username already exists";
        }

     
        Optional<User> emailExists = userRepository.findByEmail(user.getEmail());
        if (emailExists.isPresent()) {
            return "Email already exists";
        }

   
        if (!isValidEmail(user.getEmail())) {
            return "Invalid email format";
        }

   
        if (!isValidPassword(user.getPassword())) {
            return "Password must be at least 6 characters and contain at least one letter and one number";
        }

        userRepository.save(user);
        return "User registered successfully";
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public String loginUser(String email, String password) {
        Optional<User> user = userRepository.findByEmailAndPassword(email, password);
        return user.isPresent() ? "Login Successful" : "Invalid credentials";
    }

    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(regex, email);
    }

    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[A-Za-z])(?=.*\\d).{6,}$";
        return Pattern.matches(regex, password);
    }
}
