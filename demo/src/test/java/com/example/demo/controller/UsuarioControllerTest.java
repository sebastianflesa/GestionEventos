package com.example.demo.controller;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UsuarioControllerTest {

    @InjectMocks
    private UsuarioController usuarioController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Model model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void mostrarFormularioRegistro() {
        String vista = usuarioController.mostrarFormularioRegistro(model);
        verify(model).addAttribute(eq("user"), any(User.class));
        assertEquals("registro", vista);
    }

    @Test
    public void registrarUsuario_yaExiste() {
        User user = new User();
        user.setUsername("existingUser");

        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(user));

        String resultado = usuarioController.registrarUsuario(user, model);

        verify(model).addAttribute("error", "El usuario ya existe");
        assertEquals("registro", resultado);
    }

    @Test
    public void registrarUsuario_nuevoYRolExiste() {
        User user = new User();
        user.setUsername("newUser");
        user.setPassword("rawPassword");

        Role rolExistente = new Role();
        rolExistente.setName("USER");

        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(roleRepository.findByName("USER")).thenReturn(rolExistente);
        when(passwordEncoder.encode("rawPassword")).thenReturn("hashedPassword");

        String resultado = usuarioController.registrarUsuario(user, model);

        assertEquals("hashedPassword", user.getPassword());
        verify(userRepository).save(user);
        verify(model).addAttribute("success", "Usuario registrado correctamente");
        assertEquals("redirect:/login", resultado);
    }

    @Test
    public void registrarUsuario_nuevoYRolNoExiste() {
        User user = new User();
        user.setUsername("newUser");
        user.setPassword("rawPassword");

        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(roleRepository.findByName("USER")).thenReturn(null);
        when(passwordEncoder.encode("rawPassword")).thenReturn("hashedPassword");

        String resultado = usuarioController.registrarUsuario(user, model);

        verify(roleRepository).save(argThat(role -> role.getName().equals("USER")));
        verify(userRepository).save(user);
        verify(model).addAttribute("success", "Usuario registrado correctamente");
        assertEquals("redirect:/login", resultado);
    }
}
