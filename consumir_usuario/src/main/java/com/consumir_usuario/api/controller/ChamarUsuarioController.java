package com.consumir_usuario.api.controller;

import com.consumir_usuario.api.controller.request.ChamarUsuarioRequest;
import com.consumir_usuario.service.ChamarUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chamar")
@RequiredArgsConstructor
public class ChamarUsuarioController {

    private final ChamarUsuarioService service;

    @PostMapping("/consumirCriarUsuario")
    public ResponseEntity<String> chamarCriacaoUsuario(@RequestBody ChamarUsuarioRequest request) {
        return service.consumirCriarUsuario(request);
    }

    @GetMapping("/consumirBuscarUsuario/{id}")
    public ResponseEntity<String> chamarBuscarUsuario(@PathVariable Long id) {
        return service.consumirBuscarUsuario(id);
    }

    @PutMapping("/consumirAtualizarUsuario/{id}")
    public ResponseEntity<String> chamarAtualizarUsuario(@PathVariable Long id, @RequestBody ChamarUsuarioRequest request) {
        return service.consumirAtualizarUsuario(id, request);
    }

    @DeleteMapping("/consumirDeletarUsuario/{id}")
    public ResponseEntity<String> chamarDeletarUsuario(@PathVariable Long id) {
        return service.consumirDeletarUsuario(id);
    }
}
