package com.db.duckbill.service;

import com.db.duckbill.domain.entity.TarefaFinanceira;
import com.db.duckbill.domain.entity.Usuario;
import com.db.duckbill.domain.repo.TarefaFinanceiraRepository;
import com.db.duckbill.domain.repo.UsuarioRepository;
import com.db.duckbill.web.exception.AcessoNegadoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TarefaFinanceiraService {
    private final TarefaFinanceiraRepository tarefaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public TarefaFinanceira criar(Long usuarioId, TarefaFinanceira tarefa) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));
        tarefa.setUsuario(usuario);
        tarefa.setStatus(normalizarStatus(tarefa.getStatus()));
        return tarefaRepository.save(tarefa);
    }

    public List<TarefaFinanceira> listarPorUsuario(Long usuarioId) {
        return tarefaRepository.findByUsuario_IdOrderByDataLimiteAscNotificarEmAsc(usuarioId);
    }

    public List<TarefaFinanceira> listarNotificacoes(Long usuarioId) {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDateTime limit = now.plusHours(24);

        return listarPorUsuario(usuarioId).stream()
            .filter(tarefa -> !"CONCLUIDA".equals(tarefa.getStatus()))
            .filter(tarefa ->
                !tarefa.getDataLimite().isAfter(tomorrow) ||
                    !tarefa.getNotificarEm().isAfter(limit)
            )
            .sorted(Comparator.comparing(this::prioridadeSituacao)
                .thenComparing(TarefaFinanceira::getDataLimite)
                .thenComparing(TarefaFinanceira::getNotificarEm))
            .toList();
    }

    public TarefaFinanceira buscarPorId(Long id) {
        return tarefaRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Tarefa não encontrada"));
    }

    public TarefaFinanceira buscarPorIdAutorizada(Long id, Long usuarioId, boolean admin) {
        TarefaFinanceira tarefa = buscarPorId(id);
        if (!admin && !Objects.equals(tarefa.getUsuario().getId(), usuarioId)) {
            throw new AcessoNegadoException("Acesso negado à tarefa.");
        }
        return tarefa;
    }

    @Transactional
    public TarefaFinanceira atualizar(Long id, Long usuarioId, boolean admin, TarefaFinanceira dados) {
        TarefaFinanceira existente = buscarPorIdAutorizada(id, usuarioId, admin);
        existente.setTitulo(dados.getTitulo());
        existente.setDescricao(dados.getDescricao());
        existente.setValorEstimado(dados.getValorEstimado());
        existente.setDataLimite(dados.getDataLimite());
        existente.setNotificarEm(dados.getNotificarEm());
        existente.setStatus(normalizarStatus(dados.getStatus()));
        return tarefaRepository.save(existente);
    }

    @Transactional
    public TarefaFinanceira concluir(Long id, Long usuarioId, boolean admin) {
        TarefaFinanceira tarefa = buscarPorIdAutorizada(id, usuarioId, admin);
        tarefa.setStatus("CONCLUIDA");
        return tarefaRepository.save(tarefa);
    }

    @Transactional
    public void deletar(Long id, Long usuarioId, boolean admin) {
        TarefaFinanceira tarefa = buscarPorIdAutorizada(id, usuarioId, admin);
        tarefaRepository.delete(tarefa);
    }

    public String calcularSituacao(TarefaFinanceira tarefa) {
        if ("CONCLUIDA".equals(tarefa.getStatus())) {
            return "CONCLUIDA";
        }

        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        if (tarefa.getDataLimite().isBefore(today)) {
            return "ATRASADA";
        }
        if (tarefa.getDataLimite().isEqual(today)) {
            return "VENCE_HOJE";
        }
        if (!tarefa.getNotificarEm().isAfter(now)) {
            return "LEMBRETE_ATIVO";
        }
        return "AGENDADA";
    }

    private int prioridadeSituacao(TarefaFinanceira tarefa) {
        return switch (calcularSituacao(tarefa)) {
            case "ATRASADA" -> 0;
            case "VENCE_HOJE" -> 1;
            case "LEMBRETE_ATIVO" -> 2;
            case "AGENDADA" -> 3;
            default -> 4;
        };
    }

    private String normalizarStatus(String status) {
        return status == null || status.isBlank() ? "PENDENTE" : status.toUpperCase();
    }
}
