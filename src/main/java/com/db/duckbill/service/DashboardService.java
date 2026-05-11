package com.db.duckbill.service;

import com.db.duckbill.domain.entity.Categoria;
import com.db.duckbill.domain.entity.Despesa;
import com.db.duckbill.domain.repo.DespesaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final DespesaRepository despesaRepository;

    public BigDecimal totalMes(Long usuarioId, YearMonth ym) {
        return listarMes(usuarioId, ym).stream()
            .map(Despesa::getValor)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Map<String, Object>> top3Mes(Long usuarioId, YearMonth ym) {
        return listarMes(usuarioId, ym).stream()
            .collect(Collectors.groupingBy(
                Despesa::getCategoria,
                Collectors.reducing(BigDecimal.ZERO, Despesa::getValor, BigDecimal::add)
            ))
            .entrySet().stream()
            .sorted(Map.Entry.<Categoria, BigDecimal>comparingByValue().reversed())
            .limit(3)
            .map(entry -> {
                Map<String, Object> map = new HashMap<>();
                map.put("categoria", entry.getKey().getNome());
                map.put("total", entry.getValue());
                return map;
            })
            .toList();
    }

    public List<String> insightsBasicos(Long usuarioId, YearMonth ym) {
        var despesas = listarMes(usuarioId, ym);
        BigDecimal total = despesas.stream()
            .map(Despesa::getValor)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (total.signum() == 0) {
            return List.of("Sem dados no período.");
        }

        var porCategoria = despesas.stream()
            .collect(Collectors.groupingBy(
                d -> d.getCategoria().getNome(),
                Collectors.reducing(BigDecimal.ZERO, Despesa::getValor, BigDecimal::add)
            ));

        return porCategoria.entrySet().stream()
            .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
            .limit(3)
            .map(entry -> String.format(
                "Você gasta %s%% em %s. Vale reduzir?",
                entry.getValue().multiply(BigDecimal.valueOf(100)).divide(total, 0, RoundingMode.HALF_UP),
                entry.getKey()
            ))
            .toList();
    }

    private List<Despesa> listarMes(Long usuarioId, YearMonth ym) {
        return despesaRepository.findByUsuario_IdAndDataCompraBetween(usuarioId, ym.atDay(1), ym.atEndOfMonth());
    }
}
