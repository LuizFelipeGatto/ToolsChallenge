package org.example.desafiotools.service;

import org.example.desafiotools.dto.DescricaoDTO;
import org.example.desafiotools.dto.DetalhamentoTransacaoDTO;
import org.example.desafiotools.dto.FormaPagamentoDTO;
import org.example.desafiotools.dto.TransacaoDTO;
import org.example.desafiotools.enums.StatusFormaPagamento;
import org.example.desafiotools.enums.StatusTransacao;
import org.example.desafiotools.model.Transacao;
import org.example.desafiotools.repository.TransacaoRepository;
import org.example.desafiotools.util.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class TransacaoServiceTest {

    @Mock
    private TransacaoRepository transacaoRepositoryMock;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Test
    @DisplayName("Teste de salvar a transação.")
    public void testSaveTransacao() {
        DetalhamentoTransacaoDTO detalhamentoTransacaoDto = createTransacaoDTO();
        Transacao transacao = new Transacao(detalhamentoTransacaoDto, detalhamentoTransacaoDto.getDescricao().getStatus());

        when(transacaoRepositoryMock.save(any(Transacao.class))).thenReturn(transacao);

        TransacaoService transacaoService = new TransacaoService(transacaoRepositoryMock);

        TransacaoDTO resultado = transacaoService.saveTransacao(detalhamentoTransacaoDto);

        assertNotNull(resultado);
    }

    @Test
    @DisplayName("Teste de estorno da transação.")
    public void testEstornoTransacao() {
        Long id = 1L;
        Transacao transacao = transacaoEstorno(StatusTransacao.AUTORIZADO);

        when(transacaoRepositoryMock.findById(id)).thenReturn(java.util.Optional.of(transacao));
        when(transacaoRepositoryMock.save(any(Transacao.class))).thenReturn(transacao);

        TransacaoService transacaoService = new TransacaoService(transacaoRepositoryMock);

        TransacaoDTO resultado = transacaoService.estornoTransacao(id);

        assertNotNull(resultado);
        assertEquals(StatusTransacao.CANCELADO.name(), resultado.getTransacao().getDescricao().getStatus());
    }

    @Test
    @DisplayName("Teste estorno que já foi estornado.")
    public void testEstornoQueJaEstornado() {
        Long id = 1L;
        Transacao transacao = transacaoEstorno(StatusTransacao.CANCELADO);

        when(transacaoRepositoryMock.findById(id)).thenReturn(java.util.Optional.of(transacao));

        TransacaoService transacaoService = new TransacaoService(transacaoRepositoryMock);

        assertThrows(BadRequestException.class, () -> transacaoService.estornoTransacao(id));
    }

    @Test
    @DisplayName("Teste retornar transação cartão 'criptografado'.")
    public void testRetornarCartaoCriptografado() {
        TransacaoService transacaoService = new TransacaoService(transacaoRepository);
        TransacaoDTO transacaoDTO = transacaoService.buscaTransacaoPorId(1L);

        assertNotNull(transacaoDTO);
        assertEquals("12**************89", transacaoDTO.getTransacao().getCartao());
    }

    private Transacao transacaoEstorno(StatusTransacao statusTransacao) {
        Transacao transacao = new Transacao();
        transacao.setStatus(statusTransacao);
        transacao.setTipoPagamento(StatusFormaPagamento.AVISTA);
        transacao.setCartao("1234567891234567");

        return transacao;
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