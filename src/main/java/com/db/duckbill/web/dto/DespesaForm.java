package com.db.duckbill.web.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class DespesaForm {
    @NotNull
    private Long categoriaId;

    @NotNull
    @Positive
    @Digits(integer = 10, fraction = 2)
    private BigDecimal valor;

    @NotBlank
    @Pattern(regexp = "[A-Z]{3}", message = "Moeda deve ter 3 letras maiúsculas")
    private String moeda = "BRL";

    @NotNull
    @PastOrPresent
    private LocalDate dataCompra;

    @Size(max = 200)
    private String descricao;

    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public String getMoeda() { return moeda; }
    public void setMoeda(String moeda) { this.moeda = moeda; }
    public LocalDate getDataCompra() { return dataCompra; }
    public void setDataCompra(LocalDate dataCompra) { this.dataCompra = dataCompra; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
