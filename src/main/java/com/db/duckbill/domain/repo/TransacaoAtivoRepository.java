package com.db.duckbill.domain.repo;

import com.db.duckbill.domain.entity.TransacaoAtivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransacaoAtivoRepository extends JpaRepository<TransacaoAtivo, Long> {
    List<TransacaoAtivo> findByUsuario_IdOrderByDataNegocioDesc(Long usuarioId);
}
