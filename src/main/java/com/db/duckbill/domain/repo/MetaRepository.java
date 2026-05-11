package com.db.duckbill.domain.repo;

import com.db.duckbill.domain.entity.Meta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MetaRepository extends JpaRepository<Meta, Long> {
    List<Meta> findByUsuario_IdOrderByPrazoAscIdAsc(Long usuarioId);
}
