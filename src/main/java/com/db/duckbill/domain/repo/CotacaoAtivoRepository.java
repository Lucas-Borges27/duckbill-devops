package com.db.duckbill.domain.repo;

import com.db.duckbill.domain.entity.CotacaoAtivo;
import com.db.duckbill.domain.entity.CotacaoAtivoId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CotacaoAtivoRepository extends JpaRepository<CotacaoAtivo, CotacaoAtivoId> {
}
