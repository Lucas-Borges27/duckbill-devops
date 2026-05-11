package com.db.duckbill.web.dto;

public record AuthResponseDTO(
    String token,
    String tokenType,
    long expiresIn,
    UsuarioDTO usuario
) {}
