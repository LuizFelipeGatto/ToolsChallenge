package org.example.desafiotools.controller;

import lombok.RequiredArgsConstructor;
import org.example.desafiotools.dto.PagamentoDTO;
import org.example.desafiotools.dto.ResultadoOperacaoDTO;
import org.example.desafiotools.service.TransacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transacao")
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoService transacaoService;

    @PostMapping
    public ResponseEntity<String> realizarTransacao(@RequestBody PagamentoDTO pagamentoDTO) {
        ResultadoOperacaoDTO<String> resultado = transacaoService.saveTransacao(pagamentoDTO.getTransacao());
        if(!resultado.isSucesso()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado.getMensagemErro());
        }
        return ResponseEntity.ok(resultado.getResultado());
    }

    @GetMapping
    public ResponseEntity<?> buscarTransacao() {
        return ResponseEntity.ok(transacaoService.listTransacoes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> buscarTransacaoPorId(@PathVariable Long id) {
        ResultadoOperacaoDTO<String> resultado = transacaoService.buscaTransacaoPorId(id);
        return !resultado.isSucesso() ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado.getMensagemErro())
                : ResponseEntity.ok(resultado.getResultado());
    }

    @PostMapping("/estorno/{id}")
    public ResponseEntity<?> estornoTransacao(@PathVariable Long id) {
        ResultadoOperacaoDTO<String> resultado = transacaoService.estornoTransacao(id);
        return !resultado.isSucesso() ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado.getMensagemErro())
                : ResponseEntity.ok(resultado.getResultado());
    }

}
