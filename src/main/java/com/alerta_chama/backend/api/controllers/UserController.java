package com.alerta_chama.backend.api.controllers;

import com.alerta_chama.backend.api.dtos.UserRegistrationRequest;
import com.alerta_chama.backend.api.dtos.UserResponse;
import com.alerta_chama.backend.core.services.UserService;
import com.alerta_chama.backend.domain.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        User newUser = userService.registerNewUser(request);
        return new ResponseEntity<>(convertToUserResponse(newUser), HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(this::convertToUserResponse)
                .map(userResponse -> new ResponseEntity<>(userResponse, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.findAllUsers().stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    private UserResponse convertToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getPassword() // Demonstra o hash da senha, apenas para fins academicos
        );
    }
}