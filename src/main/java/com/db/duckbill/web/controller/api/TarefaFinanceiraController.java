package com.db.duckbill.web.controller.api;

import com.db.duckbill.domain.entity.TarefaFinanceira;
import com.db.duckbill.service.CurrentUserService;
import com.db.duckbill.service.TarefaFinanceiraService;
import com.db.duckbill.web.dto.TarefaFinanceiraDTO;
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

@Tag(name = "Tarefas Financeiras", description = "Gestão de tarefas e lembretes financeiros (Relógio de Ouro)")
@RestController
@RequestMapping("/api/v1/tarefas")
@RequiredArgsConstructor
public class TarefaFinanceiraController {
    private final TarefaFinanceiraService tarefaService;
    private final CurrentUserService currentUserService;

    @Operation(summary = "Criar tarefa financeira")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Tarefa criada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<TarefaFinanceiraDTO> criar(@Valid @RequestBody TarefaFinanceiraDTO dto) {
        Long usuarioId = currentUserService.resolveAccessibleUserId(dto.usuarioId());
        TarefaFinanceira tarefa = tarefaService.criar(usuarioId, toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(tarefa));
    }

    @Operation(summary = "Listar tarefas do usuário")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public List<TarefaFinanceiraDTO> listar(@RequestParam(required = false) Long usuarioId) {
        Long alvo = currentUserService.resolveAccessibleUserId(usuarioId);
        return tarefaService.listarPorUsuario(alvo).stream()
            .map(this::toDto)
            .toList();
    }

    @Operation(summary = "Listar notificações pendentes", description = "Retorna tarefas cuja janela de notificação já foi atingida e ainda estão pendentes")
    @ApiResponse(responseCode = "200", description = "Notificações retornadas")
    @GetMapping("/notificacoes")
    public List<TarefaFinanceiraDTO> listarNotificacoes(@RequestParam(required = false) Long usuarioId) {
        Long alvo = currentUserService.resolveAccessibleUserId(usuarioId);
        return tarefaService.listarNotificacoes(alvo).stream()
            .map(this::toDto)
            .toList();
    }

    @Operation(summary = "Buscar tarefa por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tarefa encontrada"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TarefaFinanceiraDTO> obter(@PathVariable Long id) {
        TarefaFinanceira tarefa = tarefaService.buscarPorIdAutorizada(id, currentUserService.getUsuarioIdAtual(), currentUserService.isAdmin());
        return ResponseEntity.ok(toDto(tarefa));
    }

    @Operation(summary = "Atualizar tarefa financeira")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tarefa atualizada"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TarefaFinanceiraDTO> atualizar(@PathVariable Long id, @Valid @RequestBody TarefaFinanceiraDTO dto) {
        Long usuarioId = currentUserService.resolveAccessibleUserId(dto.usuarioId());
        TarefaFinanceira tarefa = tarefaService.atualizar(id, usuarioId, currentUserService.isAdmin(), toEntity(dto));
        return ResponseEntity.ok(toDto(tarefa));
    }

    @Operation(summary = "Concluir tarefa", description = "Marca a tarefa como CONCLUIDA")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tarefa concluída"),
        @ApiResponse(responseCode = "400", description = "Tarefa já concluída")
    })
    @PostMapping("/{id}/concluir")
    public ResponseEntity<TarefaFinanceiraDTO> concluir(@PathVariable Long id) {
        TarefaFinanceira tarefa = tarefaService.concluir(id, currentUserService.getUsuarioIdAtual(), currentUserService.isAdmin());
        return ResponseEntity.ok(toDto(tarefa));
    }

    @Operation(summary = "Excluir tarefa financeira")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Tarefa excluída"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        tarefaService.deletar(id, currentUserService.getUsuarioIdAtual(), currentUserService.isAdmin());
        return ResponseEntity.noContent().build();
    }

    private TarefaFinanceira toEntity(TarefaFinanceiraDTO dto) {
        TarefaFinanceira tarefa = new TarefaFinanceira();
        tarefa.setTitulo(dto.titulo());
        tarefa.setDescricao(dto.descricao());
        tarefa.setValorEstimado(dto.valorEstimado());
        tarefa.setDataLimite(dto.dataLimite());
        tarefa.setNotificarEm(dto.notificarEm());
        tarefa.setStatus(dto.status());
        return tarefa;
    }

    private TarefaFinanceiraDTO toDto(TarefaFinanceira tarefa) {
        return TarefaFinanceiraDTO.fromEntity(tarefa, tarefaService.calcularSituacao(tarefa));
    }
}
