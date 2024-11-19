package org.example.desafiotools.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.desafiotools.dto.PagamentoDTO;
import org.example.desafiotools.dto.TransacaoDTO;
import org.example.desafiotools.service.TransacaoService;
import org.example.desafiotools.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacao")
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoService transacaoService;

    @PostMapping
    public ResponseEntity<TransacaoDTO> realizarTransacao(@RequestBody @Valid PagamentoDTO pagamentoDTO) {
        return ResponseUtil.processResponse(transacaoService.saveTransacao(pagamentoDTO.getTransacao()));
    }

    @GetMapping
    public ResponseEntity<List<TransacaoDTO>> buscarTransacoes() {
        return ResponseUtil.processResponse(transacaoService.listTransacoes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransacaoDTO> buscarTransacaoPorId(@PathVariable Long id) {
        return ResponseUtil.processResponse(transacaoService.buscaTransacaoPorId(id));
    }

    @PatchMapping("/estorno/{id}")
    public ResponseEntity<TransacaoDTO> estornoTransacao(@PathVariable Long id) {
        return ResponseUtil.processResponse(transacaoService.estornoTransacao(id));
    }

}
