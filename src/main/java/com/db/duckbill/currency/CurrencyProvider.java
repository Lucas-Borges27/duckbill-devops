package com.db.duckbill.currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CurrencyProvider {
    BigDecimal convert(String from, String to, BigDecimal amount);
    BigDecimal getQuote(String from, String to, LocalDate date);
}
