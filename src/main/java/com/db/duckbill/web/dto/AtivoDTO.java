package com.db.duckbill.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AtivoDTO(
    Long id,
    @NotBlank(message = "Ticker é obrigatório")
    @Size(max = 12, message = "Ticker deve ter até 12 caracteres")
    String ticker,
    @NotBlank(message = "Tipo é obrigatório")
    @Pattern(regexp = "STOCK|BOND|FUND", message = "Tipo deve ser STOCK, BOND ou FUND")
    String tipo,
    @NotBlank(message = "Moeda base é obrigatória")
    @Pattern(regexp = "[A-Z]{3}", message = "Moeda base deve ter 3 letras maiúsculas")
    String moedaBase
) {}
