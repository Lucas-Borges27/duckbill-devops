package com.db.duckbill.domain.repo;

import com.db.duckbill.domain.entity.Ativo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AtivoRepository extends JpaRepository<Ativo, Long> {
}
