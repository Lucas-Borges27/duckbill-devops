package com.db.duckbill.web.controller.api;

import com.db.duckbill.service.UsuarioService;
import com.db.duckbill.web.dto.UsuarioCreateDTO;
import com.db.duckbill.web.dto.UsuarioDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDTO> criar(@Valid @RequestBody UsuarioCreateDTO dto) {
        var saved = usuarioService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioDTO.fromEntity(saved));
    }

    @GetMapping
    public List<UsuarioDTO> listar() {
        return usuarioService.listar().stream()
            .map(UsuarioDTO::fromEntity)
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obter(@PathVariable Long id) {
        return ResponseEntity.ok(UsuarioDTO.fromEntity(usuarioService.buscarPorId(id)));
    }
}
