package com.ecommerce.api.service;

import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.repository.UserRepository;
import com.ecommerce.api.model.User;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        existingUser.setPassword(updatedUser.getPassword());

        return userRepository.save(existingUser);

    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("usuario nao encontrado com id " + id);

        }
        userRepository.deleteById(id);
    }

}
