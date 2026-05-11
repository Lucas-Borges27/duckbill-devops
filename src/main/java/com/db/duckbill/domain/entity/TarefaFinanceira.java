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
import java.time.LocalDateTime;

@Entity
@Table(name = "TAREFA_FINANCEIRA")
@Getter
@Setter
@NoArgsConstructor
public class TarefaFinanceira {
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

    @Size(max = 250)
    @Column(length = 250)
    private String descricao;

    @DecimalMin(value = "0.00", message = "Valor não pode ser negativo")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "VALOR_ESTIMADO", precision = 12, scale = 2)
    private BigDecimal valorEstimado;

    @NotNull
    @Column(name = "DATA_LIMITE", nullable = false)
    private LocalDate dataLimite;

    @NotNull
    @Column(name = "NOTIFICAR_EM", nullable = false)
    private LocalDateTime notificarEm;

    @NotBlank
    @Pattern(regexp = "PENDENTE|CONCLUIDA", message = "Status deve ser PENDENTE ou CONCLUIDA")
    @Column(nullable = false, length = 12)
    private String status = "PENDENTE";
}
