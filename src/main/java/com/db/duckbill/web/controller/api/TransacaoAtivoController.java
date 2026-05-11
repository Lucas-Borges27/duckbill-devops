package com.db.duckbill.web.controller.api;

import com.db.duckbill.domain.entity.TransacaoAtivo;
import com.db.duckbill.service.CurrentUserService;
import com.db.duckbill.service.TransacaoAtivoService;
import com.db.duckbill.web.dto.CarteiraResumoDTO;
import com.db.duckbill.web.dto.TransacaoAtivoDTO;
import com.db.duckbill.web.mapper.TransacaoAtivoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Transações de Ativos", description = "Compra e venda de ativos financeiros")
@RestController
@RequestMapping("/api/v1/transacoes-ativo")
@RequiredArgsConstructor
public class TransacaoAtivoController {
    private final TransacaoAtivoService service;
    private final CurrentUserService currentUserService;

    @Operation(summary = "Registrar transação de ativo", description = "Registra uma compra (BUY) ou venda (SELL) de ativo")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Transação registrada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<TransacaoAtivoDTO> criar(@Valid @RequestBody TransacaoAtivoDTO dto) {
        TransacaoAtivo saved = service.criar(withUsuario(dto, currentUserService.resolveAccessibleUserId(dto.usuarioId())));
        TransacaoAtivoDTO dtoSaved = TransacaoAtivoMapper.toDTO(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoSaved);
    }

    @Operation(summary = "Listar transações", description = "Admin vê todas; usuário comum vê apenas as próprias")
    @ApiResponse(responseCode = "200", description = "Lista retornada")
    @GetMapping
    public List<TransacaoAtivoDTO> listar() {
        boolean admin = currentUserService.isAdmin();
        Long usuarioId = currentUserService.getUsuarioIdAtual();

        return (admin ? service.listar() : service.listarPorUsuario(usuarioId)).stream()
            .map(TransacaoAtivoMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Operation(summary = "Resumo consolidado da carteira", description = "Agrupa posição atual por ativo (quantidade líquida e preço médio)")
    @ApiResponse(responseCode = "200", description = "Resumo retornado")
    @GetMapping("/resumo")
    public List<CarteiraResumoDTO> resumoCarteira(@RequestParam(required = false) Long usuarioId) {
        Long alvo = currentUserService.resolveAccessibleUserId(usuarioId);
        return service.resumoCarteira(alvo);
    }

    @Operation(summary = "Buscar transação por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transação encontrada"),
        @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TransacaoAtivoDTO> buscarPorId(@PathVariable Long id) {
        TransacaoAtivo transacao = service.buscarPorIdAutorizado(id, currentUserService.getUsuarioIdAtual(), currentUserService.isAdmin());
        return ResponseEntity.ok(TransacaoAtivoMapper.toDTO(transacao));
    }

    @Operation(summary = "Atualizar transação de ativo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transação atualizada"),
        @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TransacaoAtivoDTO> atualizar(@PathVariable Long id, @Valid @RequestBody TransacaoAtivoDTO dto) {
        Long usuarioId = currentUserService.resolveAccessibleUserId(dto.usuarioId());
        TransacaoAtivo updated = service.atualizar(
            id,
            withUsuario(dto, usuarioId),
            currentUserService.getUsuarioIdAtual(),
            currentUserService.isAdmin()
        );
        return ResponseEntity.ok(TransacaoAtivoMapper.toDTO(updated));
    }

    @Operation(summary = "Excluir transação de ativo")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Transação excluída"),
        @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id, currentUserService.getUsuarioIdAtual(), currentUserService.isAdmin());
        return ResponseEntity.noContent().build();
    }

    private TransacaoAtivoDTO withUsuario(TransacaoAtivoDTO dto, Long usuarioId) {
        return new TransacaoAtivoDTO(
            dto.id(),
            usuarioId,
            dto.ativoId(),
            dto.tipo(),
            dto.qtd(),
            dto.preco(),
            dto.dataNegocio()
        );
    }
}
