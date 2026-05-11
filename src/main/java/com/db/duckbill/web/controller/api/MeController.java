package com.db.duckbill.web.controller.api;

import com.db.duckbill.service.CurrentUserService;
import com.db.duckbill.service.UsuarioService;
import com.db.duckbill.web.dto.MeUpdateRequest;
import com.db.duckbill.web.dto.UsuarioDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Perfil", description = "Dados do usuário autenticado")
@RestController
@RequestMapping("/api/v1/me")
@RequiredArgsConstructor
public class MeController {
    private final CurrentUserService currentUserService;
    private final UsuarioService usuarioService;

    @Operation(summary = "Obter dados do usuário autenticado")
    @ApiResponse(responseCode = "200", description = "Dados retornados")
    @GetMapping
    public UsuarioDTO me() {
        return UsuarioDTO.fromEntity(currentUserService.getUsuarioAtual());
    }

    @Operation(summary = "Atualizar saldo do usuário autenticado")
    @ApiResponse(responseCode = "200", description = "Saldo atualizado")
    @PutMapping
    public ResponseEntity<UsuarioDTO> atualizar(@Valid @RequestBody MeUpdateRequest request) {
        Long usuarioId = currentUserService.getUsuarioIdAtual();
        return ResponseEntity.ok(UsuarioDTO.fromEntity(usuarioService.atualizarSaldo(usuarioId, request.saldo())));
    }
}
