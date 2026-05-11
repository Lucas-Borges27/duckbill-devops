package com.db.duckbill.web.controller.api;

import com.db.duckbill.domain.entity.Meta;
import com.db.duckbill.service.CurrentUserService;
import com.db.duckbill.service.MetaService;
import com.db.duckbill.web.dto.MetaAporteRequest;
import com.db.duckbill.web.dto.MetaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Metas", description = "Gestão de metas de poupança")
@RestController
@RequestMapping("/api/v1/metas")
@RequiredArgsConstructor
public class MetaController {
    private final MetaService metaService;
    private final CurrentUserService currentUserService;

    @Operation(summary = "Criar meta", description = "Registra uma nova meta de poupança")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Meta criada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<MetaDTO> criar(@Valid @RequestBody MetaDTO dto) {
        Long usuarioId = currentUserService.resolveAccessibleUserId(dto.usuarioId());
        Meta meta = metaService.criar(usuarioId, toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(MetaDTO.fromEntity(meta));
    }

    @Operation(summary = "Listar metas do usuário")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public List<MetaDTO> listar(@RequestParam(required = false) Long usuarioId) {
        Long alvo = currentUserService.resolveAccessibleUserId(usuarioId);
        return metaService.listarPorUsuario(alvo).stream()
            .map(MetaDTO::fromEntity)
            .toList();
    }

    @Operation(summary = "Buscar meta por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Meta encontrada"),
        @ApiResponse(responseCode = "404", description = "Meta não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MetaDTO> obter(@PathVariable Long id) {
        Meta meta = metaService.buscarPorIdAutorizada(id, currentUserService.getUsuarioIdAtual(), currentUserService.isAdmin());
        return ResponseEntity.ok(MetaDTO.fromEntity(meta));
    }

    @Operation(summary = "Atualizar meta")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Meta atualizada"),
        @ApiResponse(responseCode = "404", description = "Meta não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MetaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody MetaDTO dto) {
        Long usuarioId = currentUserService.resolveAccessibleUserId(dto.usuarioId());
        Meta meta = metaService.atualizar(id, usuarioId, currentUserService.isAdmin(), toEntity(dto));
        return ResponseEntity.ok(MetaDTO.fromEntity(meta));
    }

    @Operation(summary = "Registrar aporte na meta", description = "Adiciona valor ao saldo já guardado da meta")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Aporte registrado"),
        @ApiResponse(responseCode = "400", description = "Valor inválido ou meta já concluída")
    })
    @PostMapping("/{id}/aportes")
    public ResponseEntity<MetaDTO> aportar(@PathVariable Long id, @Valid @RequestBody MetaAporteRequest request) {
        Meta meta = metaService.aportar(id, currentUserService.getUsuarioIdAtual(), currentUserService.isAdmin(), request.valor());
        return ResponseEntity.ok(MetaDTO.fromEntity(meta));
    }

    @Operation(summary = "Excluir meta")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Meta excluída"),
        @ApiResponse(responseCode = "404", description = "Meta não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        metaService.deletar(id, currentUserService.getUsuarioIdAtual(), currentUserService.isAdmin());
        return ResponseEntity.noContent().build();
    }

    private Meta toEntity(MetaDTO dto) {
        Meta meta = new Meta();
        meta.setTitulo(dto.titulo());
        meta.setDescricao(dto.descricao());
        meta.setValorObjetivo(dto.valorObjetivo());
        meta.setValorGuardado(dto.valorGuardado() == null ? BigDecimal.ZERO : dto.valorGuardado());
        meta.setIcone(dto.icone());
        meta.setCorDestaque(dto.corDestaque());
        meta.setPrazo(dto.prazo());
        return meta;
    }
}
