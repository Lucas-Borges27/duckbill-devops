package com.db.duckbill.service;

import com.db.duckbill.currency.CurrencyProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CambioService {
    private final CurrencyProvider currencyProvider;

    public BigDecimal converter(BigDecimal valor, String from, String to) {
        if (from.equalsIgnoreCase(to)) {
            return valor;
        }
        return currencyProvider.convert(from, to, valor);
    }
}
