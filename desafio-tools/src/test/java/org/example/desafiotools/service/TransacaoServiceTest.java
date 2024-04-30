package org.example.desafiotools.service;

import org.example.desafiotools.dto.DescricaoDTO;
import org.example.desafiotools.dto.FormaPagamentoDTO;
import org.example.desafiotools.dto.TransacaoDTO;
import org.example.desafiotools.enums.StatusFormaPagamento;
import org.example.desafiotools.enums.StatusTransacao;
import org.example.desafiotools.model.Transacao;
import org.example.desafiotools.repository.TransacaoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;


//@DataJpaTest
//@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class TransacaoServiceTest {

    @Mock
    private TransacaoRepository transacaoRepository;

    @InjectMocks
    private TransacaoService transacaoService;

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }

    @Test
    @DisplayName("Sucesso em salvar a transação.")
    void saveTransacaoValidSuccess() {
        TransacaoDTO transacaoDTO = createTransacaoDTO();
        Transacao transacao = createTransacao(transacaoDTO);
        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacao);

//        String result = transacaoService.saveTransacao(transacaoDTO);

        // Verifica se o resultado contém JSON esperado
//        assertEquals(createExpectedJson(), result);
//        assert result != null;
    }

    @Test
    void estornoTransacao() {
    }

    @Test
    void listTransacoes() {
    }

    @Test
    void buscaTransacaoPorId() {
    }

    @Test
    void converteDigitos() {
    }

    @Test
    void numeroCartaoCriptografado() {
    }

    public TransacaoDTO createTransacaoDTO() {
        TransacaoDTO transacaoDTO = new TransacaoDTO();
        transacaoDTO.setCartao("0123456789123456");
        transacaoDTO.setFormaPagamento(createFormaPagamentoDTO());
        transacaoDTO.setDescricao(createDescricao());
        return transacaoDTO;
    }

    private FormaPagamentoDTO createFormaPagamentoDTO() {
        FormaPagamentoDTO formaPagamentoDTO = new FormaPagamentoDTO();
        formaPagamentoDTO.setTipo(StatusFormaPagamento.AVISTA.name());
        formaPagamentoDTO.setParcelas(1);
        return formaPagamentoDTO;
    }

    private DescricaoDTO createDescricao() {
        DescricaoDTO descricaoDTO = new DescricaoDTO();
        descricaoDTO.setValor(100.0);
        return descricaoDTO;
    }

    private Transacao createTransacao(TransacaoDTO transacaoDTO) {
        Transacao transacao = new Transacao(transacaoDTO);
        transacao.setId(1L);
        transacao.setDataHora(LocalDateTime.now());
        transacao.setStatus(StatusTransacao.AUTORIZADO);
        transacao.setCodigoAutorizacao("1234567890");
        transacao.setNsu("1234567890123456");
        transacao.setValor(100.0);
        transacao.setEstabelecimento("Estabelecimento");
        transacao.setTipoPagamento(StatusFormaPagamento.AVISTA);
        transacao.setParcelas(1);
        return transacao;
    }

    private String createExpectedJson() {
        return "{\"transacao\":{\"id\":1,\"cartao\":\"************3456\",\"descricao\":{\"codigoAutorizacao\":\"1234567890\",\"status\":\"AUTORIZADO\",\"nsu\":\"1234567890123456\",\"valor\":100.0,\"estabelecimento\":\"Estabelecimento\",\"dataHora\":\"2024-04-29T12:00:00\"},\"formaPagamento\":{\"tipo\":\"AVISTA\",\"parcelas\":1}}}";
    }
}