package com.ecommerce.api.service;

import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ecommerce.api.model.User;
import com.ecommerce.api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void deveRetornarListaDeUsuarios() {
        User user1 = new User("fulano de tal", "fulano@gmail.com", "6993204040", "senhateste123*" );
        User user2 = new User("ciclano da silva", "dasilva@gmail.com", "6998784467", "testedesenha321#");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> resultado = userService.getAllUsers();

        assertEquals(2, resultado.size());
    }

    @Test
    void deveRetornarUsuarioPeloId(){
        User user1 = new User("user_teste", "usuarioteste@gmail.com", "6993204040", "user1456*&");
        user1.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        User resultado = userService.getUserById(1L);

        assertEquals("user_teste", resultado.getName());
    }

    @Test
    void deveRetornarUsuarioNotFoundPeloId(){
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {userService.getUserById(999L);});

    }

    @Test
    void deveCriarUsuario(){
        User enterUser = new User("nome", "nome@gmail.com", "69993204040", "user123*");
        User outUser = new User("nome", "nome@gmail.com", "69993204040", "user123*");
        outUser.setId(1L);

        when(passwordEncoder.encode("user123*")).thenReturn("senhaHasheada123");
        when(userRepository.save(enterUser)).thenReturn(outUser);

        User resultado = userService.createUser(enterUser);

        assertEquals(1L, resultado.getId());

    }

    @Test
    void deveAtualizarUsuario(){
        User beforeUser = new User("nome", "nome@gmail.com", "69993204040", "user123*");
        User updateUser = new User("jairo", "nome@gmail.com", "69993204040", "novaSenhaTeste");
        User savedUser = new User("jairo", "nome@gmail.com", "69993204040", "senhaHash123");
        beforeUser.setId(1L);
        savedUser.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(beforeUser));
        when(passwordEncoder.encode("novaSenhaTeste")).thenReturn("senhaHash123");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User resultado = userService.updateUser(1L, updateUser);

        assertEquals("jairo", resultado.getName());

    }

    @Test
    void deveExlcuirUsuarioExistente(){
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void naoDeveExcluirUsuarioInexistente(){
        when(userRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(999L));

        verify(userRepository, never()).deleteById(anyLong());

    }

}