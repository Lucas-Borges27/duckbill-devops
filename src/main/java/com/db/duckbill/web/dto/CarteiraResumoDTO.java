package com.db.duckbill.web.dto;

import java.math.BigDecimal;

public record CarteiraResumoDTO(
    String ticker,
    BigDecimal quantidadeAtual,
    BigDecimal financeiroLiquido
) {}
