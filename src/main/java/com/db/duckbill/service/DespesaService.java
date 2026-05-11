package com.db.duckbill.service;

import com.db.duckbill.domain.entity.Despesa;
import com.db.duckbill.domain.repo.CategoriaRepository;
import com.db.duckbill.domain.repo.DespesaRepository;
import com.db.duckbill.domain.repo.UsuarioRepository;
import com.db.duckbill.web.exception.AcessoNegadoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DespesaService {
    private final DespesaRepository despesaRepo;
    private final UsuarioRepository usuarioRepo;
    private final CategoriaRepository categoriaRepo;

    @Transactional
    public Despesa criar(Despesa despesa) {
        validarDependencias(despesa);
        normalizarMoeda(despesa);
        return despesaRepo.save(despesa);
    }

    public List<Despesa> listarMes(Long usuarioId, YearMonth ym) {
        return despesaRepo.findByUsuario_IdAndDataCompraBetween(usuarioId, ym.atDay(1), ym.atEndOfMonth());
    }

    public Despesa buscarPorId(Long id) {
        return despesaRepo.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Despesa não encontrada"));
    }

    @Transactional
    public Despesa atualizar(Long id, Long usuarioId, Despesa dados) {
        Despesa existente = buscarDespesaDoUsuario(id, usuarioId);
        validarDependencias(dados);
        normalizarMoeda(dados);

        existente.setCategoria(dados.getCategoria());
        existente.setValor(dados.getValor());
        existente.setMoeda(dados.getMoeda());
        existente.setDataCompra(dados.getDataCompra());
        existente.setDescricao(dados.getDescricao());
        return despesaRepo.save(existente);
    }

    @Transactional
    public void deletar(Long id, Long usuarioId) {
        despesaRepo.delete(buscarDespesaDoUsuario(id, usuarioId));
    }

    private Despesa buscarDespesaDoUsuario(Long id, Long usuarioId) {
        Despesa existente = buscarPorId(id);
        if (!existente.getUsuario().getId().equals(usuarioId)) {
            throw new AcessoNegadoException("Acesso negado à despesa.");
        }
        return existente;
    }

    private void validarDependencias(Despesa despesa) {
        usuarioRepo.findById(despesa.getUsuario().getId())
            .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));
        categoriaRepo.findById(despesa.getCategoria().getId())
            .orElseThrow(() -> new NoSuchElementException("Categoria não encontrada"));
    }

    private void normalizarMoeda(Despesa despesa) {
        despesa.setMoeda(Optional.ofNullable(despesa.getMoeda()).orElse("BRL").toUpperCase());
    }
}
