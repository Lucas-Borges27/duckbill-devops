package com.db.duckbill.service;

import com.db.duckbill.domain.entity.Meta;
import com.db.duckbill.domain.entity.Usuario;
import com.db.duckbill.domain.repo.MetaRepository;
import com.db.duckbill.domain.repo.UsuarioRepository;
import com.db.duckbill.web.exception.AcessoNegadoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MetaService {
    private final MetaRepository metaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Meta criar(Long usuarioId, Meta meta) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));
        meta.setUsuario(usuario);
        meta.setValorGuardado(meta.getValorGuardado() == null ? BigDecimal.ZERO : meta.getValorGuardado());
        return metaRepository.save(meta);
    }

    public List<Meta> listarPorUsuario(Long usuarioId) {
        return metaRepository.findByUsuario_IdOrderByPrazoAscIdAsc(usuarioId).stream()
            .sorted(Comparator
                .comparing(Meta::getPrazo, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(Meta::getId))
            .toList();
    }

    public Meta buscarPorId(Long id) {
        return metaRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Meta não encontrada"));
    }

    public Meta buscarPorIdAutorizada(Long id, Long usuarioId, boolean admin) {
        Meta meta = buscarPorId(id);
        if (!admin && !Objects.equals(meta.getUsuario().getId(), usuarioId)) {
            throw new AcessoNegadoException("Acesso negado à meta.");
        }
        return meta;
    }

    @Transactional
    public Meta atualizar(Long id, Long usuarioId, boolean admin, Meta dados) {
        Meta existente = buscarPorIdAutorizada(id, usuarioId, admin);
        existente.setTitulo(dados.getTitulo());
        existente.setDescricao(dados.getDescricao());
        existente.setValorObjetivo(dados.getValorObjetivo());
        existente.setValorGuardado(dados.getValorGuardado());
        existente.setIcone(dados.getIcone());
        existente.setCorDestaque(dados.getCorDestaque());
        existente.setPrazo(dados.getPrazo());
        return metaRepository.save(existente);
    }

    @Transactional
    public Meta aportar(Long id, Long usuarioId, boolean admin, BigDecimal valor) {
        Meta meta = buscarPorIdAutorizada(id, usuarioId, admin);
        meta.setValorGuardado(meta.getValorGuardado().add(valor));
        return metaRepository.save(meta);
    }

    @Transactional
    public void deletar(Long id, Long usuarioId, boolean admin) {
        Meta meta = buscarPorIdAutorizada(id, usuarioId, admin);
        metaRepository.delete(meta);
    }
}
