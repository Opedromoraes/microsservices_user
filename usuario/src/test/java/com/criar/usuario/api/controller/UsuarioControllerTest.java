package com.criar.usuario.api.controller;

import com.criar.usuario.domain.dto.UsuarioDTO;
import com.criar.usuario.domain.mapper.UsuarioMapperImpl;
import com.criar.usuario.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
@Import(UsuarioMapperImpl.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService service;

    @Test
    void criarUsuarioReturns201() throws Exception {
        when(service.criarUsuario(any())).thenReturn(new UsuarioDTO(1L, "Ana", "ana@mail.com"));

        mockMvc.perform(post("/usuario/criarUsuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Ana\",\"email\":\"ana@mail.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("ana@mail.com"));
    }

    @Test
    void buscarUsuarioReturns200() throws Exception {
        when(service.buscarUsuario(1L)).thenReturn(new UsuarioDTO(1L, "Ana", "ana@mail.com"));

        mockMvc.perform(get("/usuario/buscarUsuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ana"));
    }

    @Test
    void buscarUsuarioReturns404WhenMissing() throws Exception {
        when(service.buscarUsuario(99L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "User 99 not found"));

        mockMvc.perform(get("/usuario/buscarUsuario/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletarUsuarioReturns200() throws Exception {
        mockMvc.perform(delete("/usuario/deletarUsuario/1"))
                .andExpect(status().isOk());

        verify(service).deletarUsuario(1L);
    }
}
