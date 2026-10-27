package com.consumir_usuario.api.controller;

import com.consumir_usuario.service.ChamarUsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.ResourceAccessException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChamarUsuarioController.class)
class ChamarUsuarioControllerTest {

    private static final String USER_JSON = "{\"id\":1,\"nome\":\"Ana\",\"email\":\"ana@mail.com\"}";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChamarUsuarioService service;

    @Test
    void criarUsuarioReturnsUpstreamStatusAndBody() throws Exception {
        when(service.consumirCriarUsuario(any()))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(USER_JSON));

        mockMvc.perform(post("/chamar/consumirCriarUsuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Ana\",\"email\":\"ana@mail.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void buscarUsuarioPassesThroughNotFound() throws Exception {
        when(service.consumirBuscarUsuario(99L)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        mockMvc.perform(get("/chamar/consumirBuscarUsuario/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void atualizarUsuarioReturnsUpstreamBody() throws Exception {
        when(service.consumirAtualizarUsuario(eq(1L), any()))
                .thenReturn(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(USER_JSON));

        mockMvc.perform(put("/chamar/consumirAtualizarUsuario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Ana\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ana"));
    }

    @Test
    void returns503WhenUsuarioServiceIsDown() throws Exception {
        when(service.consumirDeletarUsuario(1L)).thenThrow(new ResourceAccessException("Connection refused"));

        mockMvc.perform(delete("/chamar/consumirDeletarUsuario/1"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value(503))
                .andExpect(jsonPath("$.message").value("The usuario service is unavailable. Please try again later."));
    }
}
