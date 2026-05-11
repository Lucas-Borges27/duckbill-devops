package com.db.duckbill.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "META")
@Getter
@Setter
@NoArgsConstructor
public class Meta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USUARIO_ID")
    private Usuario usuario;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String titulo;

    @Size(max = 200)
    @Column(length = 200)
    private String descricao;

    @NotNull
    @DecimalMin(value = "0.01", message = "Objetivo deve ser maior que zero")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "VALOR_OBJETIVO", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorObjetivo;

    @NotNull
    @DecimalMin(value = "0.00", message = "Guardado não pode ser negativo")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "VALOR_GUARDADO", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorGuardado = BigDecimal.ZERO;

    @NotBlank
    @Pattern(regexp = "car|airplane|laptop|piggy-bank", message = "Ícone inválido")
    @Column(nullable = false, length = 20)
    private String icone;

    @NotBlank
    @Pattern(regexp = "#[0-9A-Fa-f]{6}", message = "Cor deve estar no formato hexadecimal")
    @Column(name = "COR_DESTAQUE", nullable = false, length = 7)
    private String corDestaque;

    @Column(name = "PRAZO")
    private LocalDate prazo;
}
