package com.db.duckbill.web.mapper;

import com.db.duckbill.domain.entity.TransacaoAtivo;
import com.db.duckbill.web.dto.TransacaoAtivoDTO;

public class TransacaoAtivoMapper {
    public static TransacaoAtivoDTO toDTO(TransacaoAtivo transacao) {
        return new TransacaoAtivoDTO(
            transacao.getId(),
            transacao.getUsuario().getId(),
            transacao.getAtivo().getId(),
            transacao.getTipo(),
            transacao.getQtd(),
            transacao.getPreco(),
            transacao.getDataNegocio()
        );
    }
}
