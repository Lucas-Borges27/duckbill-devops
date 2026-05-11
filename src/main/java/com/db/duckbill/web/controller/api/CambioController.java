package com.db.duckbill.web.controller.api;
import com.db.duckbill.service.CambioService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController 
@RequestMapping("/api/v1/cambio")
@Validated
public class CambioController {
  private final CambioService service;

  public CambioController(CambioService service) {
    this.service = service;
  }
  @GetMapping
  public Map<String,Object> convert(
      @RequestParam @NotBlank @Pattern(regexp = "[A-Z]{3}") String from,
      @RequestParam @NotBlank @Pattern(regexp = "[A-Z]{3}") String to,
      @RequestParam @Positive BigDecimal valor
  ){
    return Map.of("from", from, "to", to, "valor", valor, "convertido", service.converter(valor, from, to));
  }
}
