package com.consumir_usuario.service;

import com.consumir_usuario.api.controller.request.ChamarUsuarioRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChamarUsuarioService {

    public String consumirCriarUsuario(ChamarUsuarioRequest request) throws URISyntaxException, IOException, InterruptedException {
        log.info("Chamando serviço criar usuario");

        var mapper = new ObjectMapper();
        var requestBody = mapper.writeValueAsString(request);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/usuario/criarUsuario"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String consumirBuscarUsuario(Long id) throws IOException, InterruptedException, URISyntaxException {
        log.info("Chamando serviço buscar usuario");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/usuario/buscarUsuario/" + id))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String consumirDeletarUsuario(Long id) throws IOException, URISyntaxException, InterruptedException {
        log.info("Chamando serviço deletar usuario");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/usuario/deletarUsuario/" + id))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String consumirAtualizarUsuario(Long id, ChamarUsuarioRequest request) throws IOException, InterruptedException, URISyntaxException {
        log.info("Chamando serviço atualizar usuario");

        var mapper = new ObjectMapper();
        var requestBody = mapper.writeValueAsString(request);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/usuario/atualizarUsuario/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}