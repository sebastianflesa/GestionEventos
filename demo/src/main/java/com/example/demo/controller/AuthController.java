package com.example.demo.controller;


import com.example.demo.dto.LoginRequest;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute LoginRequest loginRequest, Model model) {
        try {
            authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), loginRequest.getPassword()
                )
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            String token = jwtUtil.generateToken(userDetails.getUsername());

            model.addAttribute("token", token);
            model.addAttribute("username", userDetails.getUsername());

            return "redirect:/dashboard";
        } catch (AuthenticationException e) {
            model.addAttribute("error", "Usuario o contraseña inválidos");
            return "login";
        }
    }
}