package org.example.desafiotools.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.desafiotools.dto.*;
import org.example.desafiotools.enums.StatusFormaPagamento;
import org.example.desafiotools.enums.StatusTransacao;
import org.example.desafiotools.service.TransacaoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
public class TransacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransacaoService transacaoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void deveRealizarTransacao() throws Exception {
        PagamentoDTO pagamentoDTO = createTransacaoDTO(125.25);
        TransacaoDTO transacaoDTO = retornoCriacaoTransacao(pagamentoDTO);

        when(transacaoService.saveTransacao(Mockito.any(DetalhamentoTransacaoDTO.class)))
                .thenReturn(transacaoDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/transacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pagamentoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transacao").exists())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        TransacaoDTO responseTransacaoDTO = objectMapper.readValue(jsonResponse, TransacaoDTO.class);

        assertNotNull(jsonResponse);
        assertNotNull(responseTransacaoDTO);
        assertEquals(transacaoDTO.getTransacao().getId().toString(), responseTransacaoDTO.getTransacao().getId().toString());
    }

    @Test
    public void deveBuscarTransacaoPorId() throws Exception {
        PagamentoDTO pagamentoDTO = createTransacaoDTO(125.25);
        TransacaoDTO transacaoDTO = retornoCriacaoTransacao(pagamentoDTO);
        Long idTransacao = pagamentoDTO.getTransacao().getId();

        when(transacaoService.buscaTransacaoPorId(idTransacao)).thenReturn(transacaoDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/transacao/{id}", idTransacao)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transacao").exists())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        TransacaoDTO responseTransacaoDTO = objectMapper.readValue(jsonResponse, TransacaoDTO.class);

        assertNotNull(jsonResponse);
        assertNotNull(responseTransacaoDTO);
        assertEquals(transacaoDTO.getTransacao().getId().toString(), responseTransacaoDTO.getTransacao().getId().toString());
    }


    @Test
    public void deveDarErroNaTransacaoFaltandoCampoValor() throws Exception {
        PagamentoDTO pagamentoDTO = createTransacaoDTO(null);
        TransacaoDTO transacaoDTO = retornoCriacaoTransacao(pagamentoDTO);

        when(transacaoService.saveTransacao(Mockito.any(DetalhamentoTransacaoDTO.class)))
                .thenReturn(transacaoDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/transacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pagamentoDTO)))
                .andExpect(status().isBadRequest());
    }

    private PagamentoDTO createTransacaoDTO(Double valor) {
        DetalhamentoTransacaoDTO detalhamentoTransacaoDto = new DetalhamentoTransacaoDTO();
        detalhamentoTransacaoDto.setId(2L);
        detalhamentoTransacaoDto.setCartao("0123456789123456789");

        DescricaoDTO descricaoDTO = new DescricaoDTO();
        descricaoDTO.setValor(valor);
        descricaoDTO.setStatus(StatusTransacao.AUTORIZADO.name());
        descricaoDTO.setEstabelecimento("Estabelecimento X");

        FormaPagamentoDTO formaPagamentoDTO = new FormaPagamentoDTO();
        formaPagamentoDTO.setTipo(StatusFormaPagamento.AVISTA.name());
        formaPagamentoDTO.setParcelas(1);

        detalhamentoTransacaoDto.setDescricao(descricaoDTO);
        detalhamentoTransacaoDto.setFormaPagamento(formaPagamentoDTO);

        PagamentoDTO pagamentoDTO = new PagamentoDTO();
        pagamentoDTO.setTransacao(detalhamentoTransacaoDto);

        return pagamentoDTO;
    }

    private TransacaoDTO retornoCriacaoTransacao(PagamentoDTO pagamentoDTO) {
        return TransacaoDTO
                .builder()
                .transacao(pagamentoDTO.getTransacao())
                .build();
    }
}

