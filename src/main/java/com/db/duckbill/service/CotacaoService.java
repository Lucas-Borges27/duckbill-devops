package com.db.duckbill.service;

import com.db.duckbill.domain.entity.CotacaoAtivo;
import com.db.duckbill.domain.entity.CotacaoAtivoId;
import com.db.duckbill.domain.entity.CotacaoMoeda;
import com.db.duckbill.domain.entity.CotacaoMoedaId;
import com.db.duckbill.domain.repo.AtivoRepository;
import com.db.duckbill.domain.repo.CotacaoAtivoRepository;
import com.db.duckbill.domain.repo.CotacaoMoedaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CotacaoService {
    private final CotacaoMoedaRepository cotacaoMoedaRepository;
    private final CotacaoAtivoRepository cotacaoAtivoRepository;
    private final AtivoRepository ativoRepository;

    public CotacaoService(CotacaoMoedaRepository cotacaoMoedaRepository, CotacaoAtivoRepository cotacaoAtivoRepository, AtivoRepository ativoRepository) {
        this.cotacaoMoedaRepository = cotacaoMoedaRepository;
        this.cotacaoAtivoRepository = cotacaoAtivoRepository;
        this.ativoRepository = ativoRepository;
    }

    // CotacaoMoeda
    @Transactional
    public CotacaoMoeda salvarCotacaoMoeda(String moeda, LocalDate dataRef, BigDecimal valorBrl) {
        CotacaoMoedaId id = new CotacaoMoedaId(moeda.toUpperCase(), dataRef);
        CotacaoMoeda cotacao = new CotacaoMoeda(id, valorBrl);
        return cotacaoMoedaRepository.save(cotacao);
    }

    public List<CotacaoMoeda> listarCotacoesMoeda() {
        return cotacaoMoedaRepository.findAll();
    }

    public CotacaoMoeda buscarCotacaoMoeda(String moeda, LocalDate dataRef) {
        CotacaoMoedaId id = new CotacaoMoedaId(moeda, dataRef);
        return cotacaoMoedaRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Cotação de moeda não encontrada"));
    }

    // CotacaoAtivo
    @Transactional
    public CotacaoAtivo salvarCotacaoAtivo(Long ativoId, LocalDate dataRef, BigDecimal precoFech) {
        ativoRepository.findById(ativoId).orElseThrow(() -> new NoSuchElementException("Ativo não encontrado"));
        CotacaoAtivoId id = new CotacaoAtivoId(ativoId, dataRef);
        CotacaoAtivo cotacao = new CotacaoAtivo(id, precoFech);
        return cotacaoAtivoRepository.save(cotacao);
    }

    public List<CotacaoAtivo> listarCotacoesAtivo() {
        return cotacaoAtivoRepository.findAll();
    }

    public CotacaoAtivo buscarCotacaoAtivo(Long ativoId, LocalDate dataRef) {
        CotacaoAtivoId id = new CotacaoAtivoId(ativoId, dataRef);
        return cotacaoAtivoRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Cotação de ativo não encontrada"));
    }
}
