package org.example.desafiotools.service;

import org.example.desafiotools.dto.DescricaoDTO;
import org.example.desafiotools.dto.DetalhamentoTransacaoDTO;
import org.example.desafiotools.dto.FormaPagamentoDTO;
import org.example.desafiotools.dto.TransacaoDTO;
import org.example.desafiotools.enums.StatusFormaPagamento;
import org.example.desafiotools.enums.StatusTransacao;
import org.example.desafiotools.model.Transacao;
import org.example.desafiotools.repository.TransacaoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@DataJpaTest
@ExtendWith(MockitoExtension.class)
class TransacaoServiceTest {

    @Mock
    private TransacaoRepository transacaoRepository;

    @Test
    @DisplayName("Teste de salvar a transação.")
    public void testSaveTransacao() {
        DetalhamentoTransacaoDTO detalhamentoTransacaoDto = createTransacaoDTO();
        Transacao transacao = new Transacao(detalhamentoTransacaoDto, detalhamentoTransacaoDto.getDescricao().getStatus());

        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacao);

        TransacaoService transacaoService = new TransacaoService(transacaoRepository);

        TransacaoDTO resultado = transacaoService.saveTransacao(detalhamentoTransacaoDto);

        assertEquals(true, Objects.nonNull(resultado));
    }

    @Test
    @DisplayName("Teste de estorno da transação.")
    public void testEstornoTransacao() {
        Long id = 1L;

        Transacao transacao = new Transacao();
        transacao.setStatus(StatusTransacao.AUTORIZADO);
        transacao.setTipoPagamento(StatusFormaPagamento.AVISTA);
        transacao.setCartao("1234567891234567");

        when(transacaoRepository.findById(id)).thenReturn(java.util.Optional.of(transacao));
        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacao);

        TransacaoService transacaoService = new TransacaoService(transacaoRepository);

        TransacaoDTO resultado = transacaoService.estornoTransacao(id);

        assertEquals(true, Objects.nonNull(resultado));
        assertEquals(StatusTransacao.CANCELADO, transacao.getStatus());
    }

    private DetalhamentoTransacaoDTO createTransacaoDTO() {
        DetalhamentoTransacaoDTO detalhamentoTransacaoDto = new DetalhamentoTransacaoDTO();
        detalhamentoTransacaoDto.setId(1L);
        detalhamentoTransacaoDto.setCartao("1234567891234567");

        DescricaoDTO descricaoDTO = new DescricaoDTO();
        descricaoDTO.setValor(125.25);
        descricaoDTO.setStatus(StatusTransacao.AUTORIZADO.name());
        descricaoDTO.setEstabelecimento("Estabelecimento X");

        FormaPagamentoDTO formaPagamentoDTO = new FormaPagamentoDTO();
        formaPagamentoDTO.setTipo(StatusFormaPagamento.AVISTA.name());
        formaPagamentoDTO.setParcelas(1);

        detalhamentoTransacaoDto.setDescricao(descricaoDTO);
        detalhamentoTransacaoDto.setFormaPagamento(formaPagamentoDTO);
        return detalhamentoTransacaoDto;
    }
}