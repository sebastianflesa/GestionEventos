package com.example.demo.service;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User user;
    private Role roleUser;
    private Role roleAdmin;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        roleUser = new Role();
        roleUser.setName("USER");

        roleAdmin = new Role();
        roleAdmin.setName("ADMIN");

        Set<Role> roles = new HashSet<>();
        roles.add(roleUser);
        roles.add(roleAdmin);

        user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPassword123");
        user.getRoles().addAll(roles);
    }

    @Test
    public void cargarUsuarioPorNombre_usuarioExiste() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserDetails detallesUsuario = userDetailsService.loadUserByUsername("testuser");

        assertNotNull(detallesUsuario);
        assertEquals("testuser", detallesUsuario.getUsername());
        assertEquals("encodedPassword123", detallesUsuario.getPassword());
        assertTrue(detallesUsuario.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
        assertTrue(detallesUsuario.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    public void cargarUsuarioPorNombre_usuarioNoExiste() {
        when(userRepository.findByUsername("notfound")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("notfound");
        });
    }
}
