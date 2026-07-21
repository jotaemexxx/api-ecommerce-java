package com.ecommerce.api.controller;

import com.ecommerce.api.service.UserService;
import com.ecommerce.api.model.User;
import com.ecommerce.api.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public ResponseEntity<List<User>> getALlUsers() {
        List<User>
    }

}
