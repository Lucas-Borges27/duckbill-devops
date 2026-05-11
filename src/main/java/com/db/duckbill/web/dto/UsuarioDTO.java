package com.db.duckbill.web.dto;

import com.db.duckbill.domain.entity.Usuario;

import java.math.BigDecimal;

public record UsuarioDTO(
    Long id,
    String nome,
    String email,
    String role,
    BigDecimal saldo
) {
    public static UsuarioDTO fromEntity(Usuario usuario) {
        return new UsuarioDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getRole(), usuario.getSaldo());
    }
}
