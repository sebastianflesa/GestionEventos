package com.example.demo.controller;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import com.example.demo.entity.Evento;
import com.example.demo.repository.EventoRepository;

public class DashboardControllerTest {

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private Model model;

    @InjectMocks
    private DashboardController dashboardController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void dashboard_devuelveVistaYAgregaAtributos() {
        String username = "usuarioPrueba";
        List<Evento> mockEventos = Arrays.asList(new Evento(), new Evento());

        when(authentication.getName()).thenReturn(username);
        when(eventoRepository.findAll()).thenReturn(mockEventos);

        String vista = dashboardController.dashboard(model, authentication);

        assertThat(vista).isEqualTo("dashboard");
        verify(model).addAttribute("username", username);
        verify(model).addAttribute("eventos", mockEventos);
    }
}
