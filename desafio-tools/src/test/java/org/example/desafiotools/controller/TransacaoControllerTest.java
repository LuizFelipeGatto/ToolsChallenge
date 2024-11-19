package org.example.desafiotools.controller;

import org.example.desafiotools.controller.TransacaoController;
import org.example.desafiotools.repository.TransacaoRepository;
import org.example.desafiotools.service.TransacaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc// Testa somente a camada web
public class TransacaoControllerTest {

    @Autowired
    private MockMvc mockMvc; // Injeta o MockMvc diretamente

    @MockBean
    private TransacaoRepository transacaoRepository;

    @MockBean
    private TransacaoService transacaoService;

    @Test
    void testCreateTransacao() throws Exception {
        when(transacaoService.saveTransacao(null))
                .thenThrow(new IllegalArgumentException("Valor não pode ser nulo"));

        mockMvc.perform(post("/transacao")
                .contentType("application/json")
                .content("{\"transacao\":null}")) // Certifique-se que o JSON está correto
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("Valor não pode ser nulo"));
    }
}