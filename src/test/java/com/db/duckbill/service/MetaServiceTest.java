package com.db.duckbill.service;

import com.db.duckbill.domain.entity.Meta;
import com.db.duckbill.domain.entity.Usuario;
import com.db.duckbill.domain.repo.MetaRepository;
import com.db.duckbill.domain.repo.UsuarioRepository;
import com.db.duckbill.web.exception.AcessoNegadoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MetaServiceTest {

    @Mock MetaRepository metaRepository;
    @Mock UsuarioRepository usuarioRepository;

    @InjectMocks MetaService metaService;

    @Test
    void criar_defineUsuarioEInicializaValorGuardadoQuandoVierNulo() {
        Usuario usuario = new Usuario();
        usuario.setId(7L);

        Meta meta = new Meta();
        meta.setTitulo("Reserva");
        meta.setValorObjetivo(new BigDecimal("1000.00"));
        meta.setValorGuardado(null);
        meta.setIcone("piggy-bank");
        meta.setCorDestaque("#23C8B1");

        when(usuarioRepository.findById(7L)).thenReturn(Optional.of(usuario));
        when(metaRepository.save(any(Meta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Meta criada = metaService.criar(7L, meta);

        assertThat(criada.getUsuario()).isEqualTo(usuario);
        assertThat(criada.getValorGuardado()).isEqualByComparingTo("0.00");
    }

    @Test
    void aportar_somaValorAoGuardado() {
        Usuario usuario = new Usuario();
        usuario.setId(3L);

        Meta meta = new Meta();
        meta.setId(9L);
        meta.setUsuario(usuario);
        meta.setValorGuardado(new BigDecimal("200.00"));

        when(metaRepository.findById(9L)).thenReturn(Optional.of(meta));
        when(metaRepository.save(any(Meta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Meta atualizada = metaService.aportar(9L, 3L, false, new BigDecimal("50.00"));

        assertThat(atualizada.getValorGuardado()).isEqualByComparingTo("250.00");
        verify(metaRepository).save(meta);
    }

    @Test
    void buscarPorIdAutorizada_bloqueiaUsuarioSemPermissao() {
        Usuario dono = new Usuario();
        dono.setId(1L);

        Meta meta = new Meta();
        meta.setId(5L);
        meta.setUsuario(dono);

        when(metaRepository.findById(5L)).thenReturn(Optional.of(meta));

        assertThatThrownBy(() -> metaService.buscarPorIdAutorizada(5L, 99L, false))
            .isInstanceOf(AcessoNegadoException.class)
            .hasMessage("Acesso negado à meta.");
    }
}
