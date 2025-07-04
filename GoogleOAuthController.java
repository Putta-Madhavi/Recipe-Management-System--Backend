package com.recipemanagement.controller;

import com.recipemanagement.model.User;
import com.recipemanagement.repository.UserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpSession;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth/google")
public class GoogleOAuthController {

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    private final UserRepository userRepository;

    public GoogleOAuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<Void> redirectToGoogle() {
        // Line 56: The scope parameter had an unencoded space between the two URLs.
        // This has been corrected by replacing the space with a '+' sign,
        // which is the standard way to concatenate multiple scopes in Google OAuth URLs.
        String googleAuthUrl = UriComponentsBuilder.fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "https://www.googleapis.com/auth/userinfo.email+https://www.googleapis.com/auth/userinfo.profile") // CORRECTED LINE
                .queryParam("access_type", "offline")
                .queryParam("prompt", "consent")
                .build()
                .toUriString();

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(googleAuthUrl)).build();
    }

    @GetMapping("/callback")
    public ResponseEntity<?> googleCallback(@RequestParam("code") String code,
                                            @RequestParam(value = "error", required = false) String error,
                                            HttpSession session) {

        if (error != null) {
            System.err.println("Google OAuth Error: " + error);
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/login?error=" + error)).build();
        }

        try {
            String tokenUrl = "https://oauth2.googleapis.com/token";
            Map<String, String> tokenRequest = new HashMap<>();
            tokenRequest.put("code", code);
            tokenRequest.put("client_id", clientId);
            tokenRequest.put("client_secret", clientSecret);
            tokenRequest.put("redirect_uri", redirectUri);
            tokenRequest.put("grant_type", "authorization_code");

            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenUrl, tokenRequest, Map.class);

            if (tokenResponse.getStatusCode() != HttpStatus.OK || tokenResponse.getBody() == null) {
                throw new RuntimeException("Failed to exchange auth code for token: " + tokenResponse.getStatusCode());
            }

            String accessToken = (String) tokenResponse.getBody().get("access_token");

            String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";
            ResponseEntity<Map> userInfoResponse = restTemplate.getForEntity(
                UriComponentsBuilder.fromUriString(userInfoUrl)
                    .queryParam("access_token", accessToken)
                    .build().toUriString(), Map.class
            );

            if (userInfoResponse.getStatusCode() != HttpStatus.OK || userInfoResponse.getBody() == null) {
                throw new RuntimeException("Failed to fetch user info from Google: " + userInfoResponse.getStatusCode());
            }

            Map<String, Object> googleProfile = userInfoResponse.getBody();
            String googleId = (String) googleProfile.get("sub");
            String email = (String) googleProfile.get("email");
            String name = (String) googleProfile.get("name");
            String picture = (String) googleProfile.get("picture");

            Optional<User> existingUserByGoogleId = userRepository.findByGoogleId(googleId);
            User user;

            if (existingUserByGoogleId.isPresent()) {
                user = existingUserByGoogleId.get();
                System.out.println("Existing Google user logged in: " + user.getEmail());
                user.setName(name);
                user.setProfilePicture(picture);
                userRepository.save(user);
            } else {
                Optional<User> userByEmail = userRepository.findByEmail(email);
                if(userByEmail.isPresent()) {
                    user = userByEmail.get();
                    user.setGoogleId(googleId);
                    user.setName(name);
                    user.setProfilePicture(picture);
                    userRepository.save(user);
                    System.out.println("Existing user linked to Google: " + user.getEmail());
                } else {
                    user = new User();
                    user.setGoogleId(googleId);
                    user.setEmail(email);
                    user.setName(name);
                    user.setProfilePicture(picture);
                    userRepository.save(user);
                    System.out.println("New Google user created: " + user.getEmail());
                }
            }

            session.setAttribute("loggedInUserId", user.getId());
            session.setAttribute("loggedInUserEmail", user.getEmail());
            session.setAttribute("isLoggedIn", true);

            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("http://127.0.0.1:5500/index.html")).build();

        } catch (Exception e) {
            System.err.println("Error during Google OAuth callback processing: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/login?error=google_auth_failed")).build();
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Google OAuth Test Endpoint Reached!");
    }
}