package com.consumir_usuario.api.controller;

import com.consumir_usuario.api.controller.request.ChamarUsuarioRequest;
import com.consumir_usuario.service.ChamarUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/chamar")
@RequiredArgsConstructor
public class ChamarUsuarioController {

    private final ChamarUsuarioService service;

    @PostMapping("/consumirCriarUsuario")
    public String chamarCriacaoUsuario(@RequestBody ChamarUsuarioRequest request) throws URISyntaxException, IOException, InterruptedException {
        String response = service.consumirCriarUsuario(request);
        return "Usuário criado com sucesso: " + (HttpStatus.CREATED);
        // postman -> POST + url + body(raw)
    }

    @GetMapping("consumirBuscarUsuario/{id}")
    public String chamarBuscarUsuario(@PathVariable Long id) throws IOException, URISyntaxException, InterruptedException {
        String response = service.consumirBuscarUsuario(id);
        return "Usuário encontrado com sucesso: " + response + ", " + (HttpStatus.OK);
        //postman -> GET + url/id + body(none)
    }

    @PutMapping("consumirAtualizarUsuario/{id}")
    public String chamarAtualizarUsuario(@PathVariable Long id, @RequestBody ChamarUsuarioRequest request) throws IOException, InterruptedException, URISyntaxException {
        String response = service.consumirAtualizarUsuario(id,request);
        return "Usuário do id {" + id + "} atualizado com sucesso: " + response + ", " + (HttpStatus.OK);
    }

    @DeleteMapping("/consumirDeletarUsuario/{id}")
    public String chamarDeletarUsuario(@PathVariable Long id) throws URISyntaxException, IOException, InterruptedException {
        service.consumirDeletarUsuario(id);
        return "Usuario deletado com sucesso: " + (HttpStatus.NO_CONTENT);
        //postman -> DELETE + url/id +body(none)
    }

}