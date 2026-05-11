package com.db.duckbill.service;

import com.db.duckbill.domain.entity.TarefaFinanceira;
import com.db.duckbill.domain.entity.Usuario;
import com.db.duckbill.domain.repo.TarefaFinanceiraRepository;
import com.db.duckbill.domain.repo.UsuarioRepository;
import com.db.duckbill.web.exception.AcessoNegadoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TarefaFinanceiraServiceTest {

    @Mock TarefaFinanceiraRepository tarefaRepository;
    @Mock UsuarioRepository usuarioRepository;

    @InjectMocks TarefaFinanceiraService tarefaFinanceiraService;

    @Test
    void criar_defineUsuarioENormalizaStatus() {
        Usuario usuario = new Usuario();
        usuario.setId(4L);

        TarefaFinanceira tarefa = new TarefaFinanceira();
        tarefa.setTitulo("Pagar conta");
        tarefa.setDataLimite(LocalDate.now().plusDays(1));
        tarefa.setNotificarEm(LocalDateTime.now().minusMinutes(10));
        tarefa.setStatus("pendente");

        when(usuarioRepository.findById(4L)).thenReturn(Optional.of(usuario));
        when(tarefaRepository.save(any(TarefaFinanceira.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TarefaFinanceira criada = tarefaFinanceiraService.criar(4L, tarefa);

        assertThat(criada.getUsuario()).isEqualTo(usuario);
        assertThat(criada.getStatus()).isEqualTo("PENDENTE");
    }

    @Test
    void listarNotificacoes_filtraConcluidasEOrdenaPorPrioridade() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        TarefaFinanceira atrasada = tarefa("Conta atrasada", usuario, LocalDate.now().minusDays(1), LocalDateTime.now().minusDays(2), "PENDENTE");
        TarefaFinanceira venceHoje = tarefa("Conta hoje", usuario, LocalDate.now(), LocalDateTime.now().minusHours(2), "PENDENTE");
        TarefaFinanceira lembreteAtivo = tarefa("Conta amanha", usuario, LocalDate.now().plusDays(1), LocalDateTime.now().minusMinutes(5), "PENDENTE");
        TarefaFinanceira agendada = tarefa("Conta futura", usuario, LocalDate.now().plusDays(10), LocalDateTime.now().plusDays(5), "PENDENTE");
        TarefaFinanceira concluida = tarefa("Concluída", usuario, LocalDate.now(), LocalDateTime.now(), "CONCLUIDA");

        when(tarefaRepository.findByUsuario_IdOrderByDataLimiteAscNotificarEmAsc(1L))
            .thenReturn(List.of(agendada, concluida, lembreteAtivo, atrasada, venceHoje));

        List<TarefaFinanceira> notificacoes = tarefaFinanceiraService.listarNotificacoes(1L);

        assertThat(notificacoes).containsExactly(atrasada, venceHoje, lembreteAtivo);
    }

    @Test
    void calcularSituacao_retornaLembreteAtivoQuandoHorarioJaPassouMasDataNaoVenceu() {
        TarefaFinanceira tarefa = new TarefaFinanceira();
        tarefa.setStatus("PENDENTE");
        tarefa.setDataLimite(LocalDate.now().plusDays(2));
        tarefa.setNotificarEm(LocalDateTime.now().minusMinutes(1));

        String situacao = tarefaFinanceiraService.calcularSituacao(tarefa);

        assertThat(situacao).isEqualTo("LEMBRETE_ATIVO");
    }

    @Test
    void buscarPorIdAutorizada_bloqueiaUsuarioSemPermissao() {
        Usuario usuario = new Usuario();
        usuario.setId(10L);

        TarefaFinanceira tarefa = tarefa("Pagar cartão", usuario, LocalDate.now(), LocalDateTime.now(), "PENDENTE");
        tarefa.setId(99L);

        when(tarefaRepository.findById(99L)).thenReturn(Optional.of(tarefa));

        assertThatThrownBy(() -> tarefaFinanceiraService.buscarPorIdAutorizada(99L, 11L, false))
            .isInstanceOf(AcessoNegadoException.class)
            .hasMessage("Acesso negado à tarefa.");
    }

    @Test
    void concluir_defineStatusConcluida() {
        Usuario usuario = new Usuario();
        usuario.setId(6L);

        TarefaFinanceira tarefa = tarefa("Fechar fatura", usuario, LocalDate.now(), LocalDateTime.now(), "PENDENTE");
        tarefa.setId(4L);

        when(tarefaRepository.findById(4L)).thenReturn(Optional.of(tarefa));
        when(tarefaRepository.save(any(TarefaFinanceira.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TarefaFinanceira concluida = tarefaFinanceiraService.concluir(4L, 6L, false);

        assertThat(concluida.getStatus()).isEqualTo("CONCLUIDA");
        verify(tarefaRepository).save(tarefa);
    }

    private TarefaFinanceira tarefa(String titulo, Usuario usuario, LocalDate dataLimite, LocalDateTime notificarEm, String status) {
        TarefaFinanceira tarefa = new TarefaFinanceira();
        tarefa.setTitulo(titulo);
        tarefa.setUsuario(usuario);
        tarefa.setValorEstimado(BigDecimal.TEN);
        tarefa.setDataLimite(dataLimite);
        tarefa.setNotificarEm(notificarEm);
        tarefa.setStatus(status);
        return tarefa;
    }
}
