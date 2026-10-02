package com.jobscape.webgateway.controller;

import com.jobscape.webgateway.client.UserClient;
import com.jobscape.webgateway.configuration.JwtUtils;
import com.jobscape.webgateway.model.User;
import com.jobscape.webgateway.service.authDto.AuthResponse;
import com.jobscape.webgateway.service.authDto.LoginRequest;
import com.jobscape.webgateway.service.authDto.RegisterRequest;
import com.jobscape.webgateway.service.authDto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserClient userClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {

        // TODO : Handle auto Login when user register server side.
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        return ResponseEntity.ok(userClient.createUser(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            if (authentication.isAuthenticated()) {
                System.out.println(request.getEmail());
                UserResponse user = userClient.getUser(request.getEmail());
                System.out.println(user.toString());
                AuthResponse authResponse = new AuthResponse(
                        "Bearer",
                        jwtUtils.generateToken(user.getId(), user.getEmail())
                );
                return ResponseEntity.ok(authResponse);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        catch (AuthenticationException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

}
