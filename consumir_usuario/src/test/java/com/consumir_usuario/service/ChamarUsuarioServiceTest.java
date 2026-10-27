package com.consumir_usuario.service;

import com.consumir_usuario.api.controller.request.ChamarUsuarioRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@RestClientTest(value = ChamarUsuarioService.class, properties = "usuario.service.url=http://usuario-service")
class ChamarUsuarioServiceTest {

    @Autowired
    private ChamarUsuarioService service;

    @Autowired
    private MockRestServiceServer server;

    @Test
    void criarUsuarioForwardsBodyAndReturnsUpstreamResponse() {
        server.expect(requestTo("http://usuario-service/usuario/criarUsuario"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(jsonPath("$.email").value("ana@mail.com"))
                .andRespond(withStatus(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"id\":1,\"nome\":\"Ana\",\"email\":\"ana@mail.com\"}"));

        ResponseEntity<String> response = service.consumirCriarUsuario(new ChamarUsuarioRequest("Ana", "ana@mail.com"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(response.getBody()).contains("\"id\":1");
        server.verify();
    }

    @Test
    void upstreamErrorStatusIsPassedThrough() {
        server.expect(requestTo("http://usuario-service/usuario/buscarUsuario/99"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"status\":404,\"message\":\"User 99 not found\"}"));

        ResponseEntity<String> response = service.consumirBuscarUsuario(99L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("User 99 not found");
    }

    @Test
    void deletarUsuarioCallsDeleteEndpoint() {
        server.expect(requestTo("http://usuario-service/usuario/deletarUsuario/1"))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withSuccess());

        assertThat(service.consumirDeletarUsuario(1L).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void unreachableServiceRaisesResourceAccessException() {
        server.expect(requestTo("http://usuario-service/usuario/buscarUsuario/1"))
                .andRespond(withException(new IOException("Connection refused")));

        assertThatThrownBy(() -> service.consumirBuscarUsuario(1L)).isInstanceOf(ResourceAccessException.class);
    }
}
