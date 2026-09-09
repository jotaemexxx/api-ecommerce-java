package com.ecommerce.api.controller;

import com.ecommerce.api.dto.LoginRequestDto;
import com.ecommerce.api.dto.LoginResponseDto;
import com.ecommerce.api.exception.UsernameNotFoundException;
import com.ecommerce.api.model.User;
import com.ecommerce.api.repository.UserRepository;
import com.ecommerce.api.security.JwtService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginRequest(@RequestBody @Valid LoginRequestDto request){
        User user = userRepository.findUserByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("credenciais inválidas"));

        String token;

        if(passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            token = jwtService.generateToken(request.getEmail());
        }
        else
        {
            throw new UsernameNotFoundException("credenciais inválidas");
        }

        LoginResponseDto response = new LoginResponseDto(token, user.getEmail());

        return ResponseEntity.ok(response);
    }



}
