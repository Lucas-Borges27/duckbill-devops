package com.db.duckbill.web.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TransacaoAtivoForm {
    @NotNull
    private Long ativoId;

    @NotBlank
    @Pattern(regexp = "BUY|SELL", message = "Tipo deve ser BUY ou SELL")
    private String tipo;

    @NotNull
    @Positive
    @Digits(integer = 8, fraction = 4)
    private BigDecimal qtd;

    @NotNull
    @Positive
    @Digits(integer = 8, fraction = 6)
    private BigDecimal preco;

    @NotNull
    @PastOrPresent
    private LocalDate dataNegocio;

    public Long getAtivoId() { return ativoId; }
    public void setAtivoId(Long ativoId) { this.ativoId = ativoId; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public BigDecimal getQtd() { return qtd; }
    public void setQtd(BigDecimal qtd) { this.qtd = qtd; }
    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public LocalDate getDataNegocio() { return dataNegocio; }
    public void setDataNegocio(LocalDate dataNegocio) { this.dataNegocio = dataNegocio; }
}
