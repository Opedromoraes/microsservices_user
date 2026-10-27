package com.consumir_usuario.service;

import com.consumir_usuario.api.controller.request.ChamarUsuarioRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class ChamarUsuarioService {

    private final RestClient restClient;

    public ChamarUsuarioService(RestClient.Builder builder, @Value("${usuario.service.url}") String usuarioServiceUrl) {
        this.restClient = builder.baseUrl(usuarioServiceUrl).build();
    }

    public ResponseEntity<String> consumirCriarUsuario(ChamarUsuarioRequest request) {
        log.info("Calling usuario service: create user");
        return forward(restClient.post().uri("/usuario/criarUsuario")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request));
    }

    public ResponseEntity<String> consumirBuscarUsuario(Long id) {
        log.info("Calling usuario service: find user {}", id);
        return forward(restClient.get().uri("/usuario/buscarUsuario/{id}", id));
    }

    public ResponseEntity<String> consumirAtualizarUsuario(Long id, ChamarUsuarioRequest request) {
        log.info("Calling usuario service: update user {}", id);
        return forward(restClient.put().uri("/usuario/atualizarUsuario/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request));
    }

    public ResponseEntity<String> consumirDeletarUsuario(Long id) {
        log.info("Calling usuario service: delete user {}", id);
        return forward(restClient.delete().uri("/usuario/deletarUsuario/{id}", id));
    }

    // 4xx/5xx from the usuario service are passed through to our caller instead of being thrown.
    private ResponseEntity<String> forward(RestClient.RequestHeadersSpec<?> request) {
        ResponseEntity<String> response = request.retrieve()
                .onStatus(status -> true, (req, res) -> { })
                .toEntity(String.class);

        var forwarded = ResponseEntity.status(response.getStatusCode());
        MediaType contentType = response.getHeaders().getContentType();
        if (contentType != null) {
            forwarded.contentType(contentType);
        }
        return forwarded.body(response.getBody());
    }
}
