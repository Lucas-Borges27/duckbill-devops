package com.db.duckbill.domain.repo;
import com.db.duckbill.domain.entity.Categoria; 
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {}