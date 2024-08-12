package com.criar.usuario.api.controller.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequest {


    private String nome;
    private String email;

}
