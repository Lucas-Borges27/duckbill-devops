package com.db.duckbill.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CotacaoMoedaDTO(
    String moeda,
    LocalDate dataRef,
    BigDecimal valorBrl
) {}
