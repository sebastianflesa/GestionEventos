package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.UserDetailsServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class AuthControllerTest {

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private Model model;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void formularioLogin_devuelveVistaLogin() {
        String vista = authController.loginForm(model);

        assertThat(vista).isEqualTo("login");
        verify(model).addAttribute(eq("loginRequest"), any(LoginRequest.class));
    }

    @Test
    public void envioLogin_autenticacionExitosa_redirigeADashboard() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("testPass");

        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testUser");
        when(jwtUtil.generateToken("testUser")).thenReturn("mockedToken");

        doAnswer(invocacion -> null).when(authManager).authenticate(any());

        String vista = authController.loginSubmit(loginRequest, model);

        assertThat(vista).isEqualTo("redirect:/dashboard");
        verify(model).addAttribute("token", "mockedToken");
        verify(model).addAttribute("username", "testUser");
    }

    @Test
    public void envioLogin_autenticacionFallida_retornaLoginConError() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("invalidUser");
        loginRequest.setPassword("invalidPass");

        doThrow(new BadCredentialsException("Bad credentials"))
            .when(authManager).authenticate(any());

        String vista = authController.loginSubmit(loginRequest, model);

        assertThat(vista).isEqualTo("login");
        verify(model).addAttribute("error", "Usuario o contraseña inválidos");
    }
}
