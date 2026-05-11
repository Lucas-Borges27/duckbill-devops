package com.db.duckbill.web.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DespesaDTO(
    Long id,
    @NotNull(message = "Usuário é obrigatório")
    Long usuarioId,
    @NotNull(message = "Categoria é obrigatória")
    Long categoriaId,
    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    @Digits(integer = 10, fraction = 2, message = "Valor inválido")
    BigDecimal valor,
    @NotBlank(message = "Moeda é obrigatória")
    @Pattern(regexp = "[A-Z]{3}", message = "Moeda deve ter 3 letras maiúsculas")
    String moeda,
    @NotNull(message = "Data da compra é obrigatória")
    @PastOrPresent(message = "Data da compra não pode estar no futuro")
    LocalDate dataCompra,
    @Size(max = 200, message = "Descrição deve ter até 200 caracteres")
    String descricao
) {}
