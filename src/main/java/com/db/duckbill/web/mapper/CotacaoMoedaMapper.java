package com.db.duckbill.web.mapper;

import com.db.duckbill.domain.entity.CotacaoMoeda;
import com.db.duckbill.web.dto.CotacaoMoedaDTO;

public class CotacaoMoedaMapper {
    public static CotacaoMoedaDTO toDTO(CotacaoMoeda cotacao) {
        return new CotacaoMoedaDTO(
            cotacao.getId().getMoeda(),
            cotacao.getId().getDataRef(),
            cotacao.getValorBrl()
        );
    }

    public static CotacaoMoeda toEntity(CotacaoMoedaDTO dto) {
        return new CotacaoMoeda(new com.db.duckbill.domain.entity.CotacaoMoedaId(dto.moeda(), dto.dataRef()), dto.valorBrl());
    }
}
