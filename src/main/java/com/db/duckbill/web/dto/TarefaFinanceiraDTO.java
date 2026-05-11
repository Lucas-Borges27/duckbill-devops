package com.db.duckbill.web.dto;

import com.db.duckbill.domain.entity.TarefaFinanceira;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TarefaFinanceiraDTO(
    Long id,
    Long usuarioId,
    @NotBlank(message = "Título é obrigatório")
    @Size(max = 100, message = "Título deve ter até 100 caracteres")
    String titulo,
    @Size(max = 250, message = "Descrição deve ter até 250 caracteres")
    String descricao,
    @DecimalMin(value = "0.00", message = "Valor estimado não pode ser negativo")
    @Digits(integer = 10, fraction = 2, message = "Valor estimado inválido")
    BigDecimal valorEstimado,
    @NotNull(message = "Data limite é obrigatória")
    LocalDate dataLimite,
    @NotNull(message = "Data da notificação é obrigatória")
    LocalDateTime notificarEm,
    @NotBlank(message = "Status é obrigatório")
    @Pattern(regexp = "PENDENTE|CONCLUIDA", message = "Status deve ser PENDENTE ou CONCLUIDA")
    String status,
    String situacao
) {
    public static TarefaFinanceiraDTO fromEntity(TarefaFinanceira tarefa, String situacao) {
        return new TarefaFinanceiraDTO(
            tarefa.getId(),
            tarefa.getUsuario().getId(),
            tarefa.getTitulo(),
            tarefa.getDescricao(),
            tarefa.getValorEstimado(),
            tarefa.getDataLimite(),
            tarefa.getNotificarEm(),
            tarefa.getStatus(),
            situacao
        );
    }
}
