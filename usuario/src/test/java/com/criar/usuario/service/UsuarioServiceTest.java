package com.criar.usuario.service;

import com.criar.usuario.domain.dto.UsuarioDTO;
import com.criar.usuario.domain.entity.Usuario;
import com.criar.usuario.domain.mapper.UsuarioMapperImpl;
import com.criar.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    private UsuarioService service;

    @BeforeEach
    void setUp() {
        service = new UsuarioService(repository, new UsuarioMapperImpl());
    }

    @Test
    void criarUsuarioSavesAndReturnsUser() {
        when(repository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        UsuarioDTO result = service.criarUsuario(UsuarioDTO.builder().nome("Ana").email("ana@mail.com").build());

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("ana@mail.com");
    }

    @Test
    void buscarUsuarioThrowsNotFoundWhenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarUsuario(99L))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void atualizarUsuarioUpdatesOnlyProvidedFields() {
        when(repository.findById(1L)).thenReturn(Optional.of(new Usuario(1L, "Ana", "ana@mail.com")));
        when(repository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        UsuarioDTO result = service.atualizarUsuario(1L, UsuarioDTO.builder().nome("Ana Maria").build());

        assertThat(result.getNome()).isEqualTo("Ana Maria");
        assertThat(result.getEmail()).isEqualTo("ana@mail.com");
    }

    @Test
    void atualizarUsuarioRejectsEmailOwnedByAnotherUser() {
        when(repository.findById(1L)).thenReturn(Optional.of(new Usuario(1L, "Ana", "ana@mail.com")));
        when(repository.findByEmail("bob@mail.com")).thenReturn(Optional.of(new Usuario(2L, "Bob", "bob@mail.com")));

        assertThatThrownBy(() -> service.atualizarUsuario(1L, UsuarioDTO.builder().email("bob@mail.com").build()))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.CONFLICT));
        verify(repository, never()).save(any());
    }
}
