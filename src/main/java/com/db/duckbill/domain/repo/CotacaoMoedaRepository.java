package com.db.duckbill.domain.repo;

import com.db.duckbill.domain.entity.CotacaoMoeda;
import com.db.duckbill.domain.entity.CotacaoMoedaId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CotacaoMoedaRepository extends JpaRepository<CotacaoMoeda, CotacaoMoedaId> {
}
