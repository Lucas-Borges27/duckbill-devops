package com.db.duckbill.service;

import com.db.duckbill.domain.entity.Categoria;
import com.db.duckbill.domain.entity.Despesa;
import com.db.duckbill.domain.entity.Usuario;
import com.db.duckbill.domain.repo.DespesaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock DespesaRepository despesaRepository;

    @InjectMocks DashboardService dashboardService;

    @Test
    void totalMes_somaValoresDoMes() {
        Usuario u = new Usuario();
        u.setId(1L);
        Categoria c = new Categoria(1L, "Alimentação");

        Despesa d1 = new Despesa();
        d1.setUsuario(u);
        d1.setCategoria(c);
        d1.setValor(new BigDecimal("10.50"));
        d1.setDataCompra(LocalDate.of(2026, 3, 1));

        Despesa d2 = new Despesa();
        d2.setUsuario(u);
        d2.setCategoria(c);
        d2.setValor(new BigDecimal("20.00"));
        d2.setDataCompra(LocalDate.of(2026, 3, 2));

        YearMonth ym = YearMonth.of(2026, 3);
        when(despesaRepository.findByUsuario_IdAndDataCompraBetween(1L, ym.atDay(1), ym.atEndOfMonth()))
            .thenReturn(List.of(d1, d2));

        BigDecimal total = dashboardService.totalMes(1L, ym);
        assertThat(total).isEqualByComparingTo("30.50");
    }

    @Test
    void top3Mes_ordenaCategoriasPorValorTotal() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Categoria alimentacao = new Categoria(1L, "Alimentação");
        Categoria transporte = new Categoria(2L, "Transporte");
        Categoria lazer = new Categoria(3L, "Lazer");

        YearMonth ym = YearMonth.of(2026, 3);
        when(despesaRepository.findByUsuario_IdAndDataCompraBetween(1L, ym.atDay(1), ym.atEndOfMonth()))
            .thenReturn(List.of(
                despesa(usuario, alimentacao, "120.00", LocalDate.of(2026, 3, 1)),
                despesa(usuario, transporte, "40.00", LocalDate.of(2026, 3, 2)),
                despesa(usuario, lazer, "90.00", LocalDate.of(2026, 3, 3)),
                despesa(usuario, alimentacao, "30.00", LocalDate.of(2026, 3, 4))
            ));

        List<Map<String, Object>> top3 = dashboardService.top3Mes(1L, ym);

        assertThat(top3).hasSize(3);
        assertThat(top3.get(0)).containsEntry("categoria", "Alimentação");
        assertThat(top3.get(1)).containsEntry("categoria", "Lazer");
        assertThat(top3.get(2)).containsEntry("categoria", "Transporte");
    }

    @Test
    void insightsBasicos_retornaMensagemQuandoNaoHaDados() {
        YearMonth ym = YearMonth.of(2026, 3);
        when(despesaRepository.findByUsuario_IdAndDataCompraBetween(1L, ym.atDay(1), ym.atEndOfMonth()))
            .thenReturn(List.of());

        List<String> insights = dashboardService.insightsBasicos(1L, ym);

        assertThat(insights).containsExactly("Sem dados no período.");
    }

    private Despesa despesa(Usuario usuario, Categoria categoria, String valor, LocalDate dataCompra) {
        Despesa despesa = new Despesa();
        despesa.setUsuario(usuario);
        despesa.setCategoria(categoria);
        despesa.setValor(new BigDecimal(valor));
        despesa.setDataCompra(dataCompra);
        return despesa;
    }
}
