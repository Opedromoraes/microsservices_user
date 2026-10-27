package com.criar.usuario.service;

import com.criar.usuario.api.controller.request.UsuarioRequest;
import com.criar.usuario.domain.dto.UsuarioDTO;
import com.criar.usuario.domain.entity.Usuario;
import com.criar.usuario.domain.mapper.UsuarioMapper;
import com.criar.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;

    public UsuarioDTO criarUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = repository.save(mapper.dtoToEntity(usuarioDTO));
        return mapper.entityToDTO(usuario);
    }

    public UsuarioDTO buscarUsuario(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + id + " not found"));
        return mapper.entityToDTO(usuario);
    }

    public UsuarioDTO atualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        UsuarioDTO usuarioEncontradoDTO = buscarUsuario(id);
        verificarUsuario(usuarioDTO, id);

        Usuario usuario = mapper.dtoToEntity(usuarioEncontradoDTO);

        if (!(usuarioDTO.getEmail() == null)) {
            usuario.setEmail(usuarioDTO.getEmail());
        }

        if (!(usuarioDTO.getNome() == null)) {
            usuario.setNome(usuarioDTO.getNome());
        }

        return mapper.entityToDTO(repository.save(usuario));
    }

    public void deletarUsuario(Long id) {
        repository.deleteById(id);
    }

    public void verificarUsuario(UsuarioDTO usuarioDTO, Long id) {
        repository.findByEmail(usuarioDTO.getEmail())
                .ifPresent(usuario -> {
                    if (!Objects.equals(usuario.getId(), id)) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail already registered");
                    }
                });
    }
}
