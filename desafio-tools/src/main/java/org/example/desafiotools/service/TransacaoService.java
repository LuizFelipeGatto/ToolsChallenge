package org.example.desafiotools.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.desafiotools.dto.DescricaoDTO;
import org.example.desafiotools.dto.DetalhamentoTransacaoDTO;
import org.example.desafiotools.dto.FormaPagamentoDTO;
import org.example.desafiotools.dto.TransacaoDTO;
import org.example.desafiotools.enums.StatusFormaPagamento;
import org.example.desafiotools.enums.StatusTransacao;
import org.example.desafiotools.model.Transacao;
import org.example.desafiotools.repository.TransacaoRepository;
import org.example.desafiotools.util.Constants;
import org.example.desafiotools.util.DataUtils;
import org.example.desafiotools.util.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class TransacaoService {

    @Autowired
    private final TransacaoRepository transacaoRepository;

    @Transactional
    public TransacaoDTO saveTransacao(DetalhamentoTransacaoDTO detalhamentoTransacaoDto) {
        StatusFormaPagamento statusFormaPagamento = StatusFormaPagamento.fromString(detalhamentoTransacaoDto.getFormaPagamento().getTipo());

        if(statusFormaPagamento.equals(StatusFormaPagamento.INVALIDO)) {
            Error.lancaErro(Constants.FORMA_PAGAMENTO_INVALIDA);
        }

        Transacao transacao = new Transacao(detalhamentoTransacaoDto);
        transacao.setTipoPagamento(statusFormaPagamento);
        transacao.setDataHora(LocalDateTime.now());
        transacao.setNsu(gerarNsu());
        transacao.setStatus(StatusTransacao.AUTORIZADO);
        transacao.setCodigoAutorizacao(gerarCodigoAutorizacao());

        transacaoRepository.save(transacao);

        return retornoTransacao(transacao);
    }

    @Transactional
    public TransacaoDTO estornoTransacao(Long id) {
        Optional<Transacao> transacaoOpt = transacaoRepository.findById(id);

        if(transacaoOpt.isEmpty()) {
            Error.lancaErro(Constants.TRANSACAO_NAO_ENCONTRADA, id);
        }

        Transacao transacaoBuscada = transacaoOpt.get();

        if(transacaoBuscada.getStatus().equals(StatusTransacao.CANCELADO)) {
            Error.lancaErro(Constants.ESTORNO_REALIZADO_ANTES, id);
        }

        transacaoBuscada.setStatus(StatusTransacao.CANCELADO);
        transacaoBuscada.setDataHora(LocalDateTime.now());
        transacaoRepository.save(transacaoBuscada);

        return retornoTransacao(transacaoBuscada);
    }

    public List<TransacaoDTO> listTransacoes() {
        return transacaoRepository.findAll()
                    .stream()
                    .map(this::retornoTransacao)
                    .collect(Collectors.toList());
    }

    public TransacaoDTO buscaTransacaoPorId(Long id) {
        return transacaoRepository.findById(id).map(this::retornoTransacao).orElse(null);
    }

    private TransacaoDTO retornoTransacao(Transacao transacao) {
        DescricaoDTO descricaoDTO = new DescricaoDTO();
        descricaoDTO.setCodigoAutorizacao(transacao.getCodigoAutorizacao());
        descricaoDTO.setStatus(transacao.getStatus().name());
        descricaoDTO.setNsu(transacao.getNsu());
        descricaoDTO.setValor(transacao.getValor());
        descricaoDTO.setEstabelecimento(transacao.getEstabelecimento());
        descricaoDTO.setDataHora(Objects.nonNull(transacao.getDataHora()) ? DataUtils.formataData(transacao.getDataHora()) : null);

        FormaPagamentoDTO formaPagamentoDTO = new FormaPagamentoDTO();
        formaPagamentoDTO.setTipo(transacao.getTipoPagamento().name());
        formaPagamentoDTO.setParcelas(transacao.getParcelas());

        DetalhamentoTransacaoDTO transacaoDto = DetalhamentoTransacaoDTO
                .builder()
                .id(transacao.getId())
                .cartao(numeroCartaoCriptografado(transacao.getCartao()))
                .descricao(descricaoDTO)
                .formaPagamento(formaPagamentoDTO)
                .build();

        return TransacaoDTO
                .builder()
                .transacao(transacaoDto)
                .build();
    }

    private int[] converteDigitos (String numeroCartao) {
        Long numero = Long.parseLong(numeroCartao);
        String numeroString = String.valueOf(numero);
        return Arrays.stream(numeroString.split("")).mapToInt(Integer::parseInt).toArray();
    }

    private String numeroCartaoCriptografado (String numeroCartao) {
        int[] digitos = converteDigitos(numeroCartao);

        List<Integer> numerosMostrados = Arrays.stream(digitos).boxed().limit(2).collect(Collectors.toList());
        numerosMostrados.addAll(Arrays.stream(digitos, 2, digitos.length - 2).mapToObj(i -> -1).collect(Collectors.toList()));
        numerosMostrados.addAll(Arrays.stream(digitos).boxed().skip(digitos.length - 2).collect(Collectors.toList()));

        return numerosMostrados.stream().map(num -> num == -1 ? "*" : String.valueOf(num)).collect(Collectors.joining(""));
    }

    private String gerarNsu() {
        long timestamp = System.currentTimeMillis();

        Random random = new Random();
        int numeroAleatorio = 100 + random.nextInt(900);

        return timestamp + String.valueOf(numeroAleatorio);
    }

    private String gerarCodigoAutorizacao() {
        Random random = new Random();
        int numero = 1000000000 + random.nextInt(900000000);
        return String.valueOf(numero);
    }

}
