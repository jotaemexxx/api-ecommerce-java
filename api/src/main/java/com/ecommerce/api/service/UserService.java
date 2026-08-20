package com.ecommerce.api.service;

import com.ecommerce.api.dto.UserPatchDto;
import com.ecommerce.api.exception.DeniedAcessException;
import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.model.Cart;
import com.ecommerce.api.model.Role;
import com.ecommerce.api.repository.CartRepository;
import com.ecommerce.api.repository.UserRepository;
import com.ecommerce.api.model.User;
import com.ecommerce.api.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    public User getUserById(Long id, Long requesterId, Role requesterRole) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado com id " + id));

        Boolean isOwner = user.getId().equals(requesterId);
        Boolean isAdmin = requesterRole == Role.ADMIN;

        if(!isOwner && !isAdmin) {
            throw new DeniedAcessException("voce nao possui permissao");
        }

        return user;
    }

    public User updateUser(Long id, User updatedUser, Long requesterId, Role requesterRole) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado com id " + id));

        Boolean isOwner = existingUser.getId().equals(requesterId);
        Boolean isAdmin = requesterRole == Role.ADMIN;

        if(!isOwner && !isAdmin) {
            throw new DeniedAcessException("voce nao possui permissao");
        }

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setNumber(updatedUser.getNumber());
        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));

        return userRepository.save(existingUser);

    }

    public User partialUpdateUser(Long id, UserPatchDto patchDto, Long requesterId, Role requesterRole) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("usuario nao encontrado com id "+ id));

        Boolean isOwner = existingUser.getId().equals(requesterId);
        Boolean isAdmin = requesterRole == Role.ADMIN;

        if(!isOwner && !isAdmin) {
            throw new DeniedAcessException("voce nao possui permissao");
        }

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
            existingUser.setPassword(passwordEncoder.encode(patchDto.getPassword()));
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

    public User promoteToAdmin(Long id, Role requesterRole){
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("usuario nao encontrado"));

        Boolean isAdmin = requesterRole == Role.ADMIN;

        if(!isAdmin){
            throw new DeniedAcessException("voce nao tem permissao para realizar essa operação");
        }

        user.setRole(Role.ADMIN);
        return userRepository.save(user);

    }

    public void deleteUser(Long id, Long requesterId, Role requesterRole){
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("usuario nao encontrado"));

        Boolean isOwner = user.getId().equals(requesterId);
        Boolean isAdmin = requesterRole == Role.ADMIN;

        if(!isOwner && !isAdmin) {
            throw new DeniedAcessException("voce nao possui permissao");
        }
        userRepository.deleteById(id);
    }

}
