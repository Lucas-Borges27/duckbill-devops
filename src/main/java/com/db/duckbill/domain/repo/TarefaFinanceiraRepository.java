package com.db.duckbill.domain.repo;

import com.db.duckbill.domain.entity.TarefaFinanceira;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TarefaFinanceiraRepository extends JpaRepository<TarefaFinanceira, Long> {
    List<TarefaFinanceira> findByUsuario_IdOrderByDataLimiteAscNotificarEmAsc(Long usuarioId);
}
