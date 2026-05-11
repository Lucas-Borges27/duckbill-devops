package com.db.duckbill.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "USUARIO")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String nome;

    @NotBlank
    @Email
    @Size(max = 120)
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Size(min = 6, max = 100)
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    @NotBlank
    @Size(max = 20)
    @Column(name = "ROLE", nullable = false)
    private String role = "ROLE_USER";

    @NotNull
    @DecimalMin(value = "0.00", message = "Saldo deve ser maior ou igual a zero")
    @Column(name = "SALDO", nullable = false, precision = 12, scale = 2)
    private BigDecimal saldo = BigDecimal.ZERO;
}
