package com.db.duckbill.service;

import com.db.duckbill.domain.entity.Categoria;
import com.db.duckbill.domain.repo.CategoriaRepository;
import com.db.duckbill.domain.repo.DespesaRepository;
import com.db.duckbill.web.exception.CategoriaEmUsoException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final DespesaRepository despesaRepository;

    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    @Transactional
    public Categoria criar(Categoria categoria) {
        categoria.setNome(categoria.getNome().trim());
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public void deletar(Long id) {
        if (despesaRepository.existsByCategoria_Id(id)) {
            throw new CategoriaEmUsoException("Categoria vinculada a despesas. Exclusão bloqueada.");
        }
        try {
            categoriaRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            throw new CategoriaEmUsoException("Categoria vinculada a despesas. Exclusão bloqueada.");
        }
    }
}
