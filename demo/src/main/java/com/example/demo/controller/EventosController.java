package com.example.demo.controller;

import java.util.List;
import java.util.UUID;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Evento;
import com.example.demo.repository.EventoRepository;

@Controller
public class EventosController {

    private final EventoRepository eventoRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public EventosController(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    @GetMapping("/eventos")
    public String EventosPaged(Model model, Authentication authentication) {
        List<Evento> eventos = eventoRepository.findAll();
        List<Evento> eventosPopulares = eventoRepository.findAll()
                                               .stream()
                                               .sorted((e1, e2) -> Integer.compare(e2.getLikes(), e1.getLikes()))
                                               .toList();
        model.addAttribute("eventos", eventos);
        model.addAttribute("eventosPopulares", eventosPopulares);
        return "eventos";
    }

    @GetMapping("/eventos/nuevo")
    public String eventoNuevo(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("evento", new Evento());
        return "evento_nuevo";
    }

    @PostMapping("/eventos/nuevo")
    public String guardarEvento(@ModelAttribute Evento evento, @RequestParam("imagenFile") MultipartFile imagenFile) throws IOException {
       if (!imagenFile.isEmpty()) {
            String nombreArchivo = UUID.randomUUID().toString() + "_" + imagenFile.getOriginalFilename();
            Path rutaImagen = Paths.get(uploadPath, nombreArchivo);
            Files.createDirectories(rutaImagen.getParent());
            Files.write(rutaImagen, imagenFile.getBytes());

            evento.setImagen("/uploads/" + nombreArchivo); 
        }

        eventoRepository.save(evento);
        return "redirect:/eventos";
    }
    
    
    @GetMapping("/eventos/ver/{id}")
    public String verEvento(@PathVariable("id") Long id, Model model) {
        Evento evento = eventoRepository.findById(id).orElse(null);
        if (evento != null) {
            model.addAttribute("evento", evento);
            return "evento_ver";
        } else {
            return "redirect:/eventos";
        }
    }

    @GetMapping("/eventos/editar/{id}")
    public String editarEvento(@PathVariable("id") Long id, Model model) {
        Evento evento = eventoRepository.findById(id).orElse(null);
        if (evento != null) {
            model.addAttribute("evento", evento);
            return "evento_editar"; 
        } else {
            return "redirect:/eventos";
        }
    }

    @PostMapping("/eventos/editar/{id}")
    public String actualizarEvento(@PathVariable("id") Long id, @ModelAttribute Evento evento, @RequestParam("imagenFile") MultipartFile imagenFile) throws IOException {
        Evento eventoExistente = eventoRepository.findById(id).orElse(null);
        if (eventoExistente != null) {
            if (!imagenFile.isEmpty()) {
                String nombreArchivo = UUID.randomUUID().toString() + "_" + imagenFile.getOriginalFilename();
                Path rutaImagen = Paths.get(uploadPath, nombreArchivo);
                Files.createDirectories(rutaImagen.getParent());
                Files.write(rutaImagen, imagenFile.getBytes());
                eventoExistente.setImagen("/uploads/" + nombreArchivo); 

                eventoExistente.setNombre(evento.getNombre());
                eventoExistente.setDescripcion(evento.getDescripcion());
                eventoExistente.setFecha(evento.getFecha());
                eventoExistente.setUbicacion(evento.getUbicacion());
                eventoExistente.setLikes(evento.getLikes());
                eventoRepository.save(eventoExistente);
            } else if (eventoExistente != null) {
                evento.setImagen(eventoExistente.getImagen());
            }

            
        }
        return "redirect:/eventos";
    }

    @PostMapping("/eventos/eliminar/{id}")
    public String borrarEvento(@PathVariable("id") Long id) {
        eventoRepository.deleteById(id);
        return "redirect:/eventos";
    }
}