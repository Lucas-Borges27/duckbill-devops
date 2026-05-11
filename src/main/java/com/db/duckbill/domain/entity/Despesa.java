package com.db.duckbill.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "DESPESA")
@Getter
@Setter
@NoArgsConstructor
public class Despesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USUARIO_ID")
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "CATEGORIA_ID")
    private Categoria categoria;

    @Positive
    @Digits(integer = 10, fraction = 2)
    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal valor;

    @Pattern(regexp = "[A-Z]{3}", message = "Moeda deve ter 3 letras maiúsculas")
    @Column(length = 3, nullable = false)
    private String moeda = "BRL";

    @PastOrPresent
    @Column(name = "DATA_COMPRA", nullable = false)
    private LocalDate dataCompra;

    @Size(max = 200)
    private String descricao;
}
