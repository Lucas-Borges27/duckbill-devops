package com.db.duckbill.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MetaAporteRequest(
    @NotNull(message = "Valor do aporte é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor do aporte deve ser maior que zero")
    @Digits(integer = 10, fraction = 2, message = "Valor do aporte inválido")
    BigDecimal valor
) {}
