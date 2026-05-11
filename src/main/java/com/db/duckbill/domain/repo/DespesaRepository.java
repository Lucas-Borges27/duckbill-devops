package com.db.duckbill.domain.repo;
import com.db.duckbill.domain.entity.*;
import org.springframework.data.jpa.repository.*;
import java.time.LocalDate;
import java.util.List;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {
  List<Despesa> findByUsuario_IdAndDataCompraBetween(Long usuarioId, LocalDate ini, LocalDate fim);
  boolean existsByCategoria_Id(Long categoriaId);
}
