package org.abhinesh.fynex.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.abhinesh.fynex.dto.AuthResponse;
import org.abhinesh.fynex.dto.LoginRequest;
import org.abhinesh.fynex.dto.RegisterRequest;
import org.abhinesh.fynex.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request
            ){
        return ResponseEntity.status(201).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
            ){
        return ResponseEntity.ok(authService.login(request));
    }
}
