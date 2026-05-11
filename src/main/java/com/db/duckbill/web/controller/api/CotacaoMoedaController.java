package com.db.duckbill.web.controller.api;

import com.db.duckbill.service.CotacaoMoedaService;
import com.db.duckbill.web.dto.CotacaoMoedaDTO;
import com.db.duckbill.web.mapper.CotacaoMoedaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cotacoes-moeda")
@RequiredArgsConstructor
public class CotacaoMoedaController {
    private final CotacaoMoedaService service;

    @GetMapping
    public List<CotacaoMoedaDTO> listar() {
        return service.listar().stream()
            .map(CotacaoMoedaMapper::toDTO)
            .collect(Collectors.toList());
    }

    @GetMapping("/{moeda}/{dataRef}")
    public ResponseEntity<CotacaoMoedaDTO> buscar(@PathVariable String moeda, @PathVariable String dataRef) {
        LocalDate data = LocalDate.parse(dataRef);
        BigDecimal valor = service.obterCotacaoExterna(moeda.toUpperCase(), data);
        CotacaoMoedaDTO dto = new CotacaoMoedaDTO(moeda.toUpperCase(), data, valor);
        return ResponseEntity.ok(dto);
    }
}
