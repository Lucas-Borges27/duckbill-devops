package com.db.duckbill.domain.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class CotacaoAtivoId implements Serializable {
    private Long ativoId;
    private LocalDate dataRef;

    public CotacaoAtivoId() {}

    public CotacaoAtivoId(Long ativoId, LocalDate dataRef) {
        this.ativoId = ativoId;
        this.dataRef = dataRef;
    }

    public Long getAtivoId() { return ativoId; }
    public void setAtivoId(Long ativoId) { this.ativoId = ativoId; }

    public LocalDate getDataRef() { return dataRef; }
    public void setDataRef(LocalDate dataRef) { this.dataRef = dataRef; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CotacaoAtivoId that = (CotacaoAtivoId) o;
        return Objects.equals(ativoId, that.ativoId) && Objects.equals(dataRef, that.dataRef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ativoId, dataRef);
    }
}
