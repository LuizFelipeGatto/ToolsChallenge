package org.example.desafiotools.controller;

import lombok.RequiredArgsConstructor;
import org.example.desafiotools.model.Transacao;
import org.example.desafiotools.service.TransacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transacao")
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoService transacaoService;

    @PostMapping
    public ResponseEntity<?> realizarTransacao(@RequestBody Transacao transacao) {
        return ResponseEntity.ok(transacaoService.saveTransacao(transacao));
    }

    @GetMapping
    public ResponseEntity<?> buscarTransacao() {
        return ResponseEntity.ok(transacaoService.listTransacoes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarTransacaoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(transacaoService.buscaTransacaoPorId(id));
    }

    @PostMapping("/estorno")
    public ResponseEntity<?> estornoTransacao(@RequestBody Transacao transacao) {
        return ResponseEntity.ok(transacaoService.estornoTransacao(transacao));
    }

}
