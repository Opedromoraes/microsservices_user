package com.criar.usuario.domain.mapper;

import com.criar.usuario.api.controller.request.UsuarioRequest;
import com.criar.usuario.api.controller.response.UsuarioResponse;
import com.criar.usuario.domain.dto.UsuarioDTO;
import com.criar.usuario.domain.entity.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioDTO entityToDTO(Usuario entity);

    Usuario dtoToEntity(UsuarioDTO usuarioDTO);

    UsuarioResponse dtoToResponse(UsuarioDTO usuarioDTO);

    UsuarioDTO requestToDto(UsuarioRequest request);

}
