package com.db.duckbill.web.mapper;

import com.db.duckbill.domain.entity.Categoria;
import com.db.duckbill.domain.entity.Despesa;
import com.db.duckbill.domain.entity.Usuario;
import com.db.duckbill.web.dto.DespesaDTO;

public class DespesaMapper {
    public static Despesa toEntity(DespesaDTO dto) {
        Despesa d = new Despesa();
        d.setId(dto.id());
        Usuario u = new Usuario();
        u.setId(dto.usuarioId());
        d.setUsuario(u);
        Categoria c = new Categoria();
        c.setId(dto.categoriaId());
        d.setCategoria(c);
        d.setValor(dto.valor());
        d.setMoeda(dto.moeda());
        d.setDataCompra(dto.dataCompra());
        d.setDescricao(dto.descricao());
        return d;
    }

    public static DespesaDTO toDTO(Despesa d) {
        return new DespesaDTO(
            d.getId(),
            d.getUsuario().getId(),
            d.getCategoria().getId(),
            d.getValor(),
            d.getMoeda(),
            d.getDataCompra(),
            d.getDescricao()
        );
    }
}
