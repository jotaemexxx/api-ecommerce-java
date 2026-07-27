package com.ecommerce.api.service;

import com.ecommerce.api.dto.UserPatchDto;
import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.model.Cart;
import com.ecommerce.api.repository.CartRepository;
import com.ecommerce.api.repository.UserRepository;
import com.ecommerce.api.model.User;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado com id " + id));
    }

    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado com id " + id));

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setNumber(updatedUser.getNumber());
        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));

        return userRepository.save(existingUser);

    }

    public User partialUpdateUser(Long id, UserPatchDto patchDto) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("usuario nao encontrado com id "+ id));

        if(patchDto.getName() != null) {
            existingUser.setName(patchDto.getName());
        }

        if(patchDto.getNumber() != null) {
            existingUser.setNumber(patchDto.getNumber());
        }

        if(patchDto.getEmail() != null) {
            existingUser.setEmail(patchDto.getEmail());
        }
        if(patchDto.getPassword() != null) {
            existingUser.setPassword(patchDto.getPassword());
        }

        return userRepository.save(existingUser);
    }

    @Transactional
    public User createUser(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        User savedUser = userRepository.save(user);

        Cart cart =  new Cart(savedUser, new ArrayList<>());
        cartRepository.save(cart);
        return savedUser;

    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("usuario nao encontrado com id " + id);

        }
        userRepository.deleteById(id);
    }

}
