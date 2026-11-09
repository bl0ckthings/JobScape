package com.jobscape.webgateway.controller;

import com.jobscape.webgateway.client.UserClient;
import com.jobscape.webgateway.configuration.JwtUtils;
import com.jobscape.webgateway.service.authDto.AuthResponse;
import com.jobscape.webgateway.service.authDto.LoginRequest;
import com.jobscape.webgateway.service.authDto.RegisterRequest;
import com.jobscape.webgateway.service.authDto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserClient userClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {

        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        return ResponseEntity.ok(userClient.createUser(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            UserResponse user = userClient.getUser(request.getEmail());
            AuthResponse authResponse = new AuthResponse(
                    "Bearer",
                    jwtUtils.generateToken(user.getId(), user.getEmail())

            );
            return ResponseEntity.ok(authResponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }



}
