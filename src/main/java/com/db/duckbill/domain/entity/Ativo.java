package com.db.duckbill.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Table(name = "ATIVO")
@Data
public class Ativo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 12)
    @Column(unique = true, nullable = false)
    private String ticker;

    @NotBlank
    @Pattern(regexp = "STOCK|BOND|FUND", message = "Tipo deve ser STOCK, BOND ou FUND")
    @Size(max = 20)
    @Column(nullable = false)
    private String tipo;

    @Pattern(regexp = "[A-Z]{3}", message = "Moeda base deve ter 3 letras maiúsculas")
    @Column(name = "MOEDA_BASE", length = 3, nullable = false)
    private String moedaBase = "BRL";
}
