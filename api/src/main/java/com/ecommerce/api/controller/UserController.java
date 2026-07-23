package com.ecommerce.api.controller;

import com.ecommerce.api.dto.UserPatchDto;
import com.ecommerce.api.dto.UserRequestDto;
import com.ecommerce.api.dto.UserResponseDto;
import com.ecommerce.api.model.User;
import com.ecommerce.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getALlUsers() {
        List<UserResponseDto> response = userService.getAllUsers().stream().map(this::toResponseDto).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {

        User user = userService.getUserById(id);

        return ResponseEntity.ok(toResponseDto(user));
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto dto) {

        User user = toEntity(dto);
        User savedUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDto(savedUser));
    }

    @PutMapping("{id}")
    public ResponseEntity<UserResponseDto> updateUser(@Valid @PathVariable Long id, @Valid @RequestBody UserRequestDto requestDto) {
        User userToUpdate = toEntity(requestDto);
        User updatedUser = userService.updateUser(id, userToUpdate);
        return ResponseEntity.ok(toResponseDto(updatedUser));
    }

    @PatchMapping("{id}")
    public ResponseEntity<UserResponseDto> partialUpdateUser(@PathVariable Long id, @Valid @RequestBody UserPatchDto patchDto) {
        User upatedUser = userService.partialUpdateUser(id, patchDto);
        return ResponseEntity.ok(toResponseDto(upatedUser));

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    private User toEntity(UserRequestDto dto) {
        return new User(dto.getName(), dto.getEmail(), dto.getNumber(), dto.getPassword());
    }

    private UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getNumber(), user.getEmail());
    }

}
