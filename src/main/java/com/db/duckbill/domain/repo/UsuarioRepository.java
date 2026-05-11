package com.db.duckbill.domain.repo;
import com.db.duckbill.domain.entity.Usuario; 
import org.springframework.data.jpa.repository.JpaRepository; 
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> { 
    Optional<Usuario> findByEmail(String email); 
    boolean existsByEmailIgnoreCase(String email);
}
