package com.db.duckbill.web.mapper;

import com.db.duckbill.domain.entity.Ativo;
import com.db.duckbill.web.dto.AtivoDTO;

public class AtivoMapper {
    public static AtivoDTO toDTO(Ativo ativo) {
        return new AtivoDTO(ativo.getId(), ativo.getTicker(), ativo.getTipo(), ativo.getMoedaBase());
    }

    public static Ativo toEntity(AtivoDTO dto) {
        Ativo ativo = new Ativo();
        ativo.setId(dto.id());
        ativo.setTicker(dto.ticker());
        ativo.setTipo(dto.tipo());
        ativo.setMoedaBase(dto.moedaBase());
        return ativo;
    }
}
