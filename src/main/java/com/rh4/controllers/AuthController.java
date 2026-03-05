package com.rh4.controllers;

import com.rh4.dto.LoginRequest;
import com.rh4.dto.StandardDTO;
import com.rh4.entities.MyUser;
import com.rh4.exception.UserException;
import com.rh4.jwt.JwtUtil;
import com.rh4.repositories.UserRepo;
import com.rh4.services.MyUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepository;
    private final JwtUtil jwtUtil;
    private final MyUserService userService;

    public AuthController(AuthenticationManager authenticationManager, UserRepo userRepository,
                          PasswordEncoder passwordEncoder, JwtUtil jwtUtil, HttpServletRequest request, MyUserService userService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<StandardDTO<Map<String, Object>>> login(@RequestBody LoginRequest loginRequest) {
        MyUser userByEmail = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + loginRequest.getEmail()));

        String email = loginRequest.getEmail();

        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, loginRequest.getPassword()));

            // Generate token and get role
            String token = jwtUtil.generateToken(
                    authentication.getName(),
                    authentication.getAuthorities().iterator().next().getAuthority()
            );
            String role = authentication.getAuthorities().iterator().next().getAuthority();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("token", token);
            responseData.put("user", userByEmail);

            return ResponseEntity.ok(
                    new StandardDTO<>(HttpStatus.OK.value(), "Login successful", responseData, null)
            );

        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new StandardDTO<>(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), null, null)
            );
        }
    }

    @GetMapping("/profile")
    public Optional<MyUser> getProfileByToken(@RequestHeader("Authorization") String authorizationHeader) throws UserException {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            return userService.getProfileByToken(token);
        } else {
            throw new UserException("Invalid Authorization header");
        }
    }
}