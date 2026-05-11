package com.db.duckbill.service;

import com.db.duckbill.domain.entity.Usuario;
import com.db.duckbill.domain.repo.UsuarioRepository;
import com.db.duckbill.web.exception.AcessoNegadoException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {
    private final UsuarioRepository usuarioRepository;

    public Usuario getUsuarioAtual() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("Usuário não autenticado");
        }

        String email = authentication.getName();
        return usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado"));
    }

    public Long getUsuarioIdAtual() {
        return getUsuarioAtual().getId();
    }

    public Long resolveAccessibleUserId(Long usuarioId) {
        Long resolvedUserId = usuarioId == null ? getUsuarioIdAtual() : usuarioId;
        validarAcessoAoUsuario(resolvedUserId);
        return resolvedUserId;
    }

    public boolean isAdmin() {
        return getUsuarioAtual().getRole().equals("ROLE_ADMIN");
    }

    public void validarAcessoAoUsuario(Long usuarioId) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("Usuário é obrigatório.");
        }
        if (!isAdmin() && !getUsuarioIdAtual().equals(usuarioId)) {
            throw new AcessoNegadoException("Acesso negado ao recurso informado.");
        }
    }
}
