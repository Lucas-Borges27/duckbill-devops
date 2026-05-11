package com.db.duckbill.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "COTACAO_ATIVO")
@Getter
@Setter
@NoArgsConstructor
public class CotacaoAtivo {
    @EmbeddedId
    private CotacaoAtivoId id;

    @NotNull
    @DecimalMin("0.000001")
    @Digits(integer = 8, fraction = 6)
    @Column(name = "PRECO_FECH", precision = 14, scale = 6, nullable = false)
    private BigDecimal precoFech;

    public CotacaoAtivo(CotacaoAtivoId id, BigDecimal precoFech) {
        this.id = id;
        this.precoFech = precoFech;
    }
}
