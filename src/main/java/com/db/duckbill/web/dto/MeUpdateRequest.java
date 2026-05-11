package com.db.duckbill.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MeUpdateRequest(
    @NotNull(message = "Saldo é obrigatório")
    @DecimalMin(value = "0.00", message = "Saldo deve ser maior ou igual a zero")
    BigDecimal saldo
) {}
