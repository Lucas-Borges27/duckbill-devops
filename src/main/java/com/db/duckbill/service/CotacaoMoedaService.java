package com.db.duckbill.service;

import com.db.duckbill.currency.CurrencyProvider;
import com.db.duckbill.domain.entity.CotacaoMoeda;
import com.db.duckbill.domain.entity.CotacaoMoedaId;
import com.db.duckbill.domain.repo.CotacaoMoedaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CotacaoMoedaService {

    private final CotacaoMoedaRepository repo;
    private final CurrencyProvider currencyProvider;

    @Transactional(readOnly = true)
    public List<CotacaoMoeda> listar() {
        return repo.findAll();
    }

    @Transactional
    public CotacaoMoeda salvar(CotacaoMoeda cotacao) {
        return repo.save(cotacao);
    }

    public BigDecimal obterCotacaoExterna(String moeda, LocalDate data) {
        return currencyProvider.getQuote(moeda, "BRL", data);
    }
}
