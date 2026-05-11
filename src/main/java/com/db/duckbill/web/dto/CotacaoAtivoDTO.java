package com.db.duckbill.web.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CotacaoAtivoDTO(
    @NotNull(message = "Ativo é obrigatório")
    Long ativoId,
    @NotNull(message = "Data de referência é obrigatória")
    @PastOrPresent(message = "Data de referência não pode estar no futuro")
    LocalDate dataRef,
    @NotNull(message = "Preço de fechamento é obrigatório")
    @Positive(message = "Preço de fechamento deve ser positivo")
    @Digits(integer = 8, fraction = 6, message = "Preço de fechamento inválido")
    BigDecimal precoFech
) {}
