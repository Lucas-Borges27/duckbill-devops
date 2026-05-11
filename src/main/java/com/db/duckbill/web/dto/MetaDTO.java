package com.db.duckbill.web.dto;

import com.db.duckbill.domain.entity.Meta;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MetaDTO(
    Long id,
    Long usuarioId,
    @NotBlank(message = "Título é obrigatório")
    @Size(max = 100, message = "Título deve ter até 100 caracteres")
    String titulo,
    @Size(max = 200, message = "Descrição deve ter até 200 caracteres")
    String descricao,
    @NotNull(message = "Valor objetivo é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor objetivo deve ser maior que zero")
    @Digits(integer = 10, fraction = 2, message = "Valor objetivo inválido")
    BigDecimal valorObjetivo,
    @NotNull(message = "Valor guardado é obrigatório")
    @DecimalMin(value = "0.00", message = "Valor guardado não pode ser negativo")
    @Digits(integer = 10, fraction = 2, message = "Valor guardado inválido")
    BigDecimal valorGuardado,
    @NotBlank(message = "Ícone é obrigatório")
    @Pattern(regexp = "car|airplane|laptop|piggy-bank", message = "Ícone inválido")
    String icone,
    @NotBlank(message = "Cor de destaque é obrigatória")
    @Pattern(regexp = "#[0-9A-Fa-f]{6}", message = "Cor deve estar no formato hexadecimal")
    String corDestaque,
    LocalDate prazo
) {
    public static MetaDTO fromEntity(Meta meta) {
        return new MetaDTO(
            meta.getId(),
            meta.getUsuario().getId(),
            meta.getTitulo(),
            meta.getDescricao(),
            meta.getValorObjetivo(),
            meta.getValorGuardado(),
            meta.getIcone(),
            meta.getCorDestaque(),
            meta.getPrazo()
        );
    }
}
