package org.abhinesh.fynex.services;

import lombok.RequiredArgsConstructor;
import org.abhinesh.fynex.dto.AuthResponse;
import org.abhinesh.fynex.dto.LoginRequest;
import org.abhinesh.fynex.dto.RegisterRequest;
import org.abhinesh.fynex.entity.User;
import org.abhinesh.fynex.repository.UserRepository;
import org.abhinesh.fynex.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request){
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        if (userRepository.existsByUsername(request.getUsername())){
            throw new RuntimeException("Username already taken");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .active(true)
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(token, user.getUsername(), user.getRole().name(),"Registration Successful");
    }


    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found!"));
        if (!user.isActive()){
            throw new RuntimeException("User is inactive");
        }

        String token =  jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(token, user.getUsername()  , user.getRole().name(), "Login Successful");
    }
}
