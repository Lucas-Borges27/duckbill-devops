package com.db.duckbill.web.mapper;

import com.db.duckbill.domain.entity.CotacaoAtivo;
import com.db.duckbill.web.dto.CotacaoAtivoDTO;

public class CotacaoAtivoMapper {
    public static CotacaoAtivoDTO toDTO(CotacaoAtivo cotacao) {
        return new CotacaoAtivoDTO(
            cotacao.getId().getAtivoId(),
            cotacao.getId().getDataRef(),
            cotacao.getPrecoFech()
        );
    }

    public static CotacaoAtivo toEntity(CotacaoAtivoDTO dto) {
        return new CotacaoAtivo(new com.db.duckbill.domain.entity.CotacaoAtivoId(dto.ativoId(), dto.dataRef()), dto.precoFech());
    }
}
