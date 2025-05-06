package com.example.demo.controller;

import com.example.demo.entity.Evento;
import com.example.demo.repository.EventoRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
public class EventosControllerTest { 

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventoRepository eventoRepository;

    @Test
    @DisplayName("Mostrar lista de eventos en /eventos")
    @WithMockUser(username = "testUser")
    public void mostrarListaEventos() throws Exception {
        Evento evento1 = new Evento();
        evento1.setId(1L);
        evento1.setNombre("Evento 1");
        evento1.setDescripcion("Descripcion del Evento 1");
        evento1.setFecha(LocalDate.now());
        evento1.setUbicacion("Ubicacion 1");
        evento1.setLikes(10);

        Evento evento2 = new Evento();
        evento2.setId(2L);
        evento2.setNombre("Evento 2");
        evento2.setDescripcion("Descripcion del Evento 2");
        evento2.setFecha(LocalDate.now());
        evento2.setUbicacion("Ubicacion 2");
        evento2.setLikes(20);

        Mockito.when(eventoRepository.findAll()).thenReturn(Arrays.asList(evento1, evento2));

        mockMvc.perform(get("/eventos"))
                .andExpect(status().isOk())
                .andExpect(view().name("eventos"))
                .andExpect(model().attributeExists("eventos"))
                .andExpect(model().attributeExists("eventosPopulares"))
                .andExpect(model().attribute("eventos", Arrays.asList(evento1, evento2)));
    }

    @Test
    @DisplayName("Mostrar formulario para crear un nuevo evento en /eventos/nuevo")
    @WithMockUser(username = "testUser")
    public void mostrarFormularioNuevoEvento() throws Exception {
        mockMvc.perform(get("/eventos/nuevo"))
                .andExpect(status().isOk())
                .andExpect(view().name("evento_nuevo"))
                .andExpect(model().attributeExists("evento"));
    }

    @Test
    @DisplayName("Guardar un nuevo evento con imagen en /eventos/nuevo")
    @WithMockUser(username = "testUser")
    public void guardarNuevoEventoConImagen() throws Exception {
        Evento evento = new Evento();
        evento.setId(1L);
        evento.setNombre("Evento 1");
        evento.setDescripcion("Descripcion del Evento 1");
        evento.setFecha(LocalDate.now());
        evento.setUbicacion("Ubicacion 1");
        evento.setLikes(10);
        evento.setImagen("/uploads/testImage.jpg");

        Mockito.when(eventoRepository.save(Mockito.any(Evento.class))).thenReturn(evento);

        mockMvc.perform(multipart("/eventos/nuevo")
                        .file("imagenFile", "testImageContent".getBytes())
                        .param("nombre", "Evento 1")
                        .param("descripcion", "Descripcion del Evento 1")
                        .param("fecha", "2023-10-01")
                        .param("ubicacion", "Ubicacion 1")
                        .param("likes", "10")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/eventos"));
    }

    @Test
    @DisplayName("Ver detalles de un evento en /eventos/ver/{id}")
    @WithMockUser(username = "testUser")
    public void verDetallesEvento() throws Exception {
        Evento evento = new Evento();
        evento.setId(1L);
        evento.setNombre("Evento 1");
        evento.setDescripcion("Descripcion del Evento 1");
        evento.setFecha(LocalDate.now());
        evento.setUbicacion("Ubicacion 1");
        evento.setLikes(10);

        Mockito.when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));

        mockMvc.perform(get("/eventos/ver/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("evento_ver"))
                .andExpect(model().attributeExists("evento"))
                .andExpect(model().attribute("evento", evento));
    }

    @Test
    @DisplayName("Editar un evento existente en /eventos/editar/{id}")
    @WithMockUser(username = "testUser")
    public void editarEvento() throws Exception {
        Evento evento = new Evento();
        evento.setId(1L);
        evento.setNombre("Evento 1");
        evento.setDescripcion("Descripcion del Evento 1");
        evento.setFecha(LocalDate.now());
        evento.setUbicacion("Ubicacion 1");
        evento.setLikes(10);

        Mockito.when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));

        mockMvc.perform(get("/eventos/editar/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("evento_editar"))
                .andExpect(model().attributeExists("evento"))
                .andExpect(model().attribute("evento", evento));
    }

    @Test
    @DisplayName("Actualizar un evento existente en /eventos/editar/{id}")
    @WithMockUser(username = "testUser")
    public void actualizarEvento() throws Exception {
        Evento eventoExistente = new Evento();
        eventoExistente.setId(1L);
        eventoExistente.setNombre("Evento 1");
        eventoExistente.setDescripcion("Descripcion del Evento 1");
        eventoExistente.setFecha(LocalDate.now());
        eventoExistente.setUbicacion("Ubicacion 1");
        eventoExistente.setLikes(10);

        Mockito.when(eventoRepository.findById(1L)).thenReturn(Optional.of(eventoExistente));
        Mockito.when(eventoRepository.save(Mockito.any(Evento.class))).thenReturn(eventoExistente);

        mockMvc.perform(multipart("/eventos/editar/1")
                        .file("imagenFile", "testImageContent".getBytes())
                        .param("nombre", "Evento Actualizado")
                        .param("descripcion", "Descripcion Actualizada")
                        .param("fecha", "2023-10-02")
                        .param("ubicacion", "Ubicacion Actualizada")
                        .param("likes", "15")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/eventos"));
    }

    @Test
    @DisplayName("Eliminar un evento en /eventos/eliminar/{id}")
    @WithMockUser(username = "testUser")
    public void eliminarEvento() throws Exception {
        Mockito.doNothing().when(eventoRepository).deleteById(1L);

        mockMvc.perform(post("/eventos/eliminar/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/eventos"));

        Mockito.verify(eventoRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Mostrar el dashboard en /")
    @WithMockUser(username = "testUser")
    public void mostrarDashboard() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/eventos"));
    }
}
