package com.db.duckbill.web.controller.api;

import com.db.duckbill.service.CurrentUserService;
import com.db.duckbill.service.DashboardService;
import com.db.duckbill.service.DespesaService;
import com.db.duckbill.web.dto.DespesaDTO;
import com.db.duckbill.web.mapper.DespesaMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "Despesas", description = "Gestão de despesas do usuário")
@RestController
@RequestMapping("/api/v1/despesas")
@RequiredArgsConstructor
public class DespesaController {
    private final DespesaService service;
    private final DashboardService dashboardService;
    private final CurrentUserService currentUserService;

    @Operation(summary = "Criar despesa", description = "Registra uma nova despesa para o usuário autenticado")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Despesa criada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping
    public ResponseEntity<DespesaDTO> criar(@Valid @RequestBody DespesaDTO dto) {
        Long usuarioId = currentUserService.resolveAccessibleUserId(dto.usuarioId());
        var saved = service.criar(DespesaMapper.toEntity(withUsuario(dto, usuarioId)));
        DespesaDTO dtoSaved = DespesaMapper.toDTO(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoSaved);
    }

    @Operation(summary = "Listar despesas", description = "Retorna despesas do mês informado (padrão: mês atual)")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public List<DespesaDTO> listar(
        @Parameter(description = "ID do usuário (admin pode filtrar por outros usuários)") @RequestParam(required = false) Long usuarioId,
        @Parameter(description = "Mês no formato yyyy-MM (ex: 2026-05)") @RequestParam(required = false) String mes) {
        Long alvo = resolverUsuarioId(usuarioId);
        var ym = mes == null || mes.isBlank() ? YearMonth.now() : YearMonth.parse(mes);
        return service.listarMes(alvo, ym).stream()
            .map(DespesaMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Operation(summary = "Buscar despesa por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Despesa encontrada"),
        @ApiResponse(responseCode = "404", description = "Despesa não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DespesaDTO> obter(@PathVariable Long id) {
        var despesa = service.buscarPorId(id);
        currentUserService.validarAcessoAoUsuario(despesa.getUsuario().getId());
        return ResponseEntity.ok(DespesaMapper.toDTO(despesa));
    }

    @Operation(summary = "Atualizar despesa")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Despesa atualizada"),
        @ApiResponse(responseCode = "404", description = "Despesa não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DespesaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody DespesaDTO dto) {
        Long usuarioId = currentUserService.resolveAccessibleUserId(dto.usuarioId());
        var payload = DespesaMapper.toEntity(withUsuario(dto, usuarioId));

        return ResponseEntity.ok(DespesaMapper.toDTO(service.atualizar(id, usuarioId, payload)));
    }

    @Operation(summary = "Excluir despesa")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Despesa excluída"),
        @ApiResponse(responseCode = "404", description = "Despesa não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        var despesa = service.buscarPorId(id);
        currentUserService.validarAcessoAoUsuario(despesa.getUsuario().getId());
        service.deletar(id, despesa.getUsuario().getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Top 3 categorias do mês", description = "Retorna as 3 categorias com maior gasto no mês")
    @ApiResponse(responseCode = "200", description = "Top 3 retornado")
    @GetMapping("/top3")
    public List<Map<String, Object>> top3(@RequestParam(required = false) Long usuarioId, @RequestParam String mes) {
        Long alvo = resolverUsuarioId(usuarioId);
        var ym = YearMonth.parse(mes);
        return dashboardService.top3Mes(alvo, ym);
    }

    @Operation(summary = "Insights financeiros do mês", description = "Gera frases analíticas sobre o padrão de gastos do mês")
    @ApiResponse(responseCode = "200", description = "Insights gerados")
    @GetMapping("/insights")
    public ResponseEntity<List<String>> insights(@RequestParam(required = false) Long usuarioId, @RequestParam String mes) {
        Long alvo = resolverUsuarioId(usuarioId);
        return ResponseEntity.ok(dashboardService.insightsBasicos(alvo, YearMonth.parse(mes)));
    }

    private Long resolverUsuarioId(Long usuarioId) {
        return currentUserService.resolveAccessibleUserId(usuarioId);
    }

    private DespesaDTO withUsuario(DespesaDTO dto, Long usuarioId) {
        return new DespesaDTO(
            dto.id(),
            usuarioId,
            dto.categoriaId(),
            dto.valor(),
            dto.moeda(),
            dto.dataCompra(),
            dto.descricao()
        );
    }
}
