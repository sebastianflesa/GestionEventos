package com.example.demo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.demo.security.JwtUtil;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    private JwtUtil jwtUtil;
    private String token;
    private final String username = "testuser";

    @BeforeEach
    public void setUp() {
        jwtUtil = new JwtUtil();
        token = jwtUtil.generateToken(username);
    }

    @Test
    public void generarTokenNoNulo() {
        assertNotNull(token, "Token generado no debe ser null");
    }

    @Test
    public void extraerNombreUsuario() {
        String usernameExtraido = jwtUtil.extractUsername(token);
        assertEquals(username, usernameExtraido, "El nombre de usuario extraido debe coincidir");
    }

    @Test
    public void validarTokenValido() {
        assertTrue(jwtUtil.validateToken(token), "El token generado debe ser valido");
    }

    @Test
    public void validarTokenInvalido() {
        String tokenInvalido = token.substring(0, token.length() - 1) + "x";
        assertFalse(jwtUtil.validateToken(tokenInvalido), "Un token modificado no debe ser valido");
    }
}
