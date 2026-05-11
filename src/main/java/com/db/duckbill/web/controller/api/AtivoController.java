package com.db.duckbill.web.controller.api;

import com.db.duckbill.domain.entity.Ativo;
import com.db.duckbill.service.AtivoService;
import com.db.duckbill.web.dto.AtivoDTO;
import com.db.duckbill.web.mapper.AtivoMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/ativos")
public class AtivoController {
    private final AtivoService service;

    public AtivoController(AtivoService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AtivoDTO> criar(@Valid @RequestBody AtivoDTO dto) {
        Ativo saved = service.criar(AtivoMapper.toEntity(dto));
        AtivoDTO dtoSaved = AtivoMapper.toDTO(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoSaved);
    }

    @GetMapping
    public List<AtivoDTO> listar() {
        return service.listar().stream()
            .map(AtivoMapper::toDTO)
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AtivoDTO buscarPorId(@PathVariable Long id) {
        Ativo ativo = service.buscarPorId(id);
        return AtivoMapper.toDTO(ativo);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AtivoDTO atualizar(@PathVariable Long id, @Valid @RequestBody AtivoDTO dto) {
        Ativo ativo = AtivoMapper.toEntity(dto);
        return AtivoMapper.toDTO(service.atualizar(id, ativo));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
