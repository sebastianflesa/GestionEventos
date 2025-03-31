package com.example.demo.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Evento;
import com.example.demo.repository.EventoRepository;

@Controller
public class DashboardController {
    @Autowired
    private EventoRepository eventoRepository;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        List<Evento> eventos = eventoRepository.findAll();
        model.addAttribute("username", username);
        model.addAttribute("eventos", eventos);
        return "dashboard";
    }
}