package com.example.demo.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;
    private Role adminRole;
    private Role userRole;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setPassword("encryptedPassword123");

        adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");

        userRole = new Role();
        userRole.setName("ROLE_USER");

        user.getRoles().add(adminRole);
        user.getRoles().add(userRole);
    }

    @Test
    public void obtenerYEstablecerPassword() {
        assertEquals("encryptedPassword123", user.getPassword());
    }

    @Test
    public void obtenerAutoridades() {
        var authorities = user.getAuthorities();
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertEquals(2, authorities.size());
    }

    @Test
    public void cuentaNoExpirada() {
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    public void cuentaNoBloqueada() {
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    public void credencialesNoExpiradas() {
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    public void cuentaHabilitada() {
        assertTrue(user.isEnabled());
    }

    @Test
    public void obtenerColeccionRoles() {
        Set<Role> roles = user.getRoles();
        assertEquals(2, roles.size());
        assertTrue(roles.contains(adminRole));
        assertTrue(roles.contains(userRole));
    }
}
