package com.db.duckbill.web.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoAtivoDTO(
    Long id,
    @NotNull(message = "Usuário é obrigatório")
    Long usuarioId,
    @NotNull(message = "Ativo é obrigatório")
    Long ativoId,
    @NotBlank(message = "Tipo é obrigatório")
    @Pattern(regexp = "BUY|SELL", message = "Tipo deve ser BUY ou SELL")
    String tipo,
    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser positiva")
    @Digits(integer = 8, fraction = 4, message = "Quantidade inválida")
    BigDecimal qtd,
    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser positivo")
    @Digits(integer = 8, fraction = 6, message = "Preço inválido")
    BigDecimal preco,
    @NotNull(message = "Data do negócio é obrigatória")
    @PastOrPresent(message = "Data do negócio não pode estar no futuro")
    LocalDate dataNegocio
) {}
