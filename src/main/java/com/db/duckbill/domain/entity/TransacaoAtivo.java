package com.db.duckbill.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TRANSACAO_ATIVO")
@Getter
@Setter
@NoArgsConstructor
public class TransacaoAtivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USUARIO_ID")
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ATIVO_ID")
    private Ativo ativo;

    @NotBlank
    @Pattern(regexp = "BUY|SELL", message = "Tipo deve ser BUY ou SELL")
    @Size(max = 4)
    @Column(nullable = false)
    private String tipo; // BUY or SELL

    @Positive
    @Digits(integer = 8, fraction = 4)
    @Column(precision = 12, scale = 4, nullable = false)
    private BigDecimal qtd;

    @Positive
    @Digits(integer = 8, fraction = 6)
    @Column(precision = 14, scale = 6, nullable = false)
    private BigDecimal preco;

    @PastOrPresent
    @Column(name = "DATA_NEGOCIO", nullable = false)
    private LocalDate dataNegocio;
}
