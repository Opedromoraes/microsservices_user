package com.criar.usuario.api.controller;

import com.criar.usuario.api.controller.request.UsuarioRequest;
import com.criar.usuario.api.controller.response.UsuarioResponse;
import com.criar.usuario.domain.dto.UsuarioDTO;
import com.criar.usuario.domain.mapper.UsuarioMapper;
import com.criar.usuario.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService service;
    private final UsuarioMapper mapper;

    @PostMapping("/criarUsuario")
    public ResponseEntity<UsuarioResponse> criarUsuario(@RequestBody UsuarioRequest request){
        log.info("Recebendo requisição criar usuário");

        UsuarioDTO usuarioDTO = service.criarUsuario(mapper.requestToDto(request));
        UsuarioResponse response = mapper.dtoToResponse(usuarioDTO);
        return ResponseEntity.status(CREATED).body(response);
    }

    @GetMapping("/buscarUsuario/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuario(@PathVariable Long id){
        log.info("Recebendo requisição buscar usuário");
        UsuarioDTO usuarioDTO = service.buscarUsuario(id);
        UsuarioResponse response = mapper.dtoToResponse(usuarioDTO);
        return ResponseEntity.status(OK).body(response);
    }

    @PutMapping("/atualizarUsuario/{id}")
    public ResponseEntity<UsuarioResponse> atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioRequest request){
        log.info("Recebendo requisição atualizar usuário");
        UsuarioDTO usuarioDTO = service.atualizarUsuario(id,mapper.requestToDto(request));
        UsuarioResponse response = mapper.dtoToResponse(usuarioDTO);
        return ResponseEntity.status(OK).body(response);
    }


    @DeleteMapping("/deletarUsuario/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id){
        log.info("Recebendo requisição deletar usuário");

        service.deletarUsuario(id);
        return ResponseEntity.status(OK).build();
    }
}
