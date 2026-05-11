package com.db.duckbill.domain.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class CotacaoMoedaId implements Serializable {
    private String moeda;
    private LocalDate dataRef;

    public CotacaoMoedaId() {}

    public CotacaoMoedaId(String moeda, LocalDate dataRef) {
        this.moeda = moeda;
        this.dataRef = dataRef;
    }

    public String getMoeda() { return moeda; }
    public void setMoeda(String moeda) { this.moeda = moeda; }

    public LocalDate getDataRef() { return dataRef; }
    public void setDataRef(LocalDate dataRef) { this.dataRef = dataRef; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CotacaoMoedaId that = (CotacaoMoedaId) o;
        return Objects.equals(moeda, that.moeda) && Objects.equals(dataRef, that.dataRef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moeda, dataRef);
    }
}
