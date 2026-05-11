package com.db.duckbill.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "COTACAO_MOEDA")
@Getter
@Setter
@NoArgsConstructor
public class CotacaoMoeda {
    @EmbeddedId
    private CotacaoMoedaId id;

    @NotNull
    @DecimalMin("0.000001")
    @Digits(integer = 8, fraction = 6)
    @Column(name = "VALOR_BRL", precision = 14, scale = 6, nullable = false)
    private BigDecimal valorBrl;

    public CotacaoMoeda(CotacaoMoedaId id, BigDecimal valorBrl) {
        this.id = id;
        this.valorBrl = valorBrl;
    }
}
