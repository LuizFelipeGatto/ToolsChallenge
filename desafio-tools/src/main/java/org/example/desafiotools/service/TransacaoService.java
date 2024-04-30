package org.example.desafiotools.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.desafiotools.dto.DescricaoDTO;
import org.example.desafiotools.dto.FormaPagamentoDTO;
import org.example.desafiotools.dto.ResultadoOperacaoDTO;
import org.example.desafiotools.dto.TransacaoDTO;
import org.example.desafiotools.enums.StatusFormaPagamento;
import org.example.desafiotools.enums.StatusTransacao;
import org.example.desafiotools.model.Transacao;
import org.example.desafiotools.repository.TransacaoRepository;
import org.example.desafiotools.util.Constants;
import org.example.desafiotools.util.DataUtils;
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
    public ResultadoOperacaoDTO<String> saveTransacao(TransacaoDTO transacaoDTO) {
        Transacao transacao = new Transacao(transacaoDTO);
        transacao.setTipoPagamento(setarTipoPagamento(transacaoDTO.getFormaPagamento().getTipo()));

        if (!Constants.getTiposFormaPagamento().contains(transacao.getTipoPagamento().name())) {
            return new ResultadoOperacaoDTO<>(false, null, Constants.FORMA_PAGAMENTO_INVALIDA);
        }

        if (verificaDigitosCartao(transacao.getCartao())) {
            if(transacao.getCartao().length() > 19) {
                return new ResultadoOperacaoDTO<>(false, null, Constants.DIGITOS_EXCESSO);
            }

            if(transacao.getCartao().length() < 15) {
                return new ResultadoOperacaoDTO<>(false, null, Constants.DIGITOS_FALTANTES);
            }
        } else {
            return new ResultadoOperacaoDTO<>(false, null, Constants.DIGITOS_LETRAS);
        }

        transacao.setDataHora(LocalDateTime.now());
        transacao.setNsu(gerarNsu());
        transacao.setStatus(StatusTransacao.AUTORIZADO);
        transacao.setCodigoAutorizacao(gerarCodigoAutorizacao());

        Transacao transacaoSalva = transacaoRepository.save(transacao);

        return new ResultadoOperacaoDTO<>(true, converteToJson(populaDto(transacaoSalva)), null);
    }

    @Transactional
    public ResultadoOperacaoDTO<String> estornoTransacao(Long id) {
        Transacao transacaoBuscada = transacaoRepository.findById(id).orElse(null);
        if(Objects.isNull(transacaoBuscada)) {
            return new ResultadoOperacaoDTO<>(false, null, Constants.TRANSACAO_NAO_ENCONTRADA);
        }

        if(transacaoBuscada.getStatus().equals(StatusTransacao.CANCELADO)) {
            return new ResultadoOperacaoDTO<>(false, null, Constants.ESTORNO_REALIZADO_ANTES);
        }

        transacaoBuscada.setStatus(StatusTransacao.CANCELADO);
        transacaoBuscada.setDataHora(LocalDateTime.now());
        transacaoRepository.save(transacaoBuscada);

        return new ResultadoOperacaoDTO<>(true, converteToJson(populaDto(transacaoBuscada)), null);
    }

    public List<TransacaoDTO> listTransacoes() {
        List<Transacao> lista = transacaoRepository.findAll();
        if(!lista.isEmpty()) {
            return lista.stream().map(this::populaDto).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public ResultadoOperacaoDTO<String> buscaTransacaoPorId(Long id) {
        Optional<Transacao> transacaoBuscada = transacaoRepository.findById(id);
        if(transacaoBuscada.isPresent()) {
            return new ResultadoOperacaoDTO<>(true, converteToJson(populaDto(transacaoBuscada.get())), null);
        }
        return new ResultadoOperacaoDTO<>(false, null, Constants.TRANSACAO_NAO_ENCONTRADA);
    }

    private boolean verificaDigitosCartao (String numeroCartao) {
        return numeroCartao.matches("[0-9]+");
    }

    private TransacaoDTO populaDto(Transacao transacao) {

        DescricaoDTO descricaoDTO = new DescricaoDTO();
        descricaoDTO.setCodigoAutorizacao(transacao.getCodigoAutorizacao());
        descricaoDTO.setStatus(transacao.getStatus().name());
        descricaoDTO.setNsu(transacao.getNsu());
        descricaoDTO.setValor(transacao.getValor());
        descricaoDTO.setEstabelecimento(transacao.getEstabelecimento());
        descricaoDTO.setDataHora(DataUtils.formataData(transacao.getDataHora()));

        FormaPagamentoDTO formaPagamentoDTO = new FormaPagamentoDTO();
        formaPagamentoDTO.setTipo(transacao.getTipoPagamento().name());
        formaPagamentoDTO.setParcelas(transacao.getParcelas());

        return TransacaoDTO
                .builder()
                .id(transacao.getId())
                .cartao(numeroCartaoCriptografado(transacao.getCartao()))
                .descricao(descricaoDTO)
                .formaPagamento(formaPagamentoDTO)
                .build();
    }

    private String converteToJson(TransacaoDTO transacaoDTO) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("transacao", new Gson().toJsonTree(transacaoDTO));
        return new Gson().toJson(jsonObject);
    }

    private StatusFormaPagamento setarTipoPagamento(String tipoPagamento) {
       if(tipoPagamento.equals(StatusFormaPagamento.AVISTA.name())) {
           return StatusFormaPagamento.AVISTA;
       } else if(tipoPagamento.equals(StatusFormaPagamento.PARCELADO_LOJA.name())) {
           return StatusFormaPagamento.PARCELADO_LOJA;
       } else if (tipoPagamento.equals(StatusFormaPagamento.PARCELADO_EMISSOR.name())) {
           return StatusFormaPagamento.PARCELADO_EMISSOR;
       } else {
           return StatusFormaPagamento.INVALIDO;
       }
    }

    public int[] converteDigitos (String numeroCartao) {
        Long numero = Long.parseLong(numeroCartao);
        String numeroString = String.valueOf(numero);
        return Arrays.stream(numeroString.split("")).mapToInt(Integer::parseInt).toArray();
    }

    public String numeroCartaoCriptografado (String numeroCartao) {
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

        return String.valueOf(timestamp) + String.valueOf(numeroAleatorio);
    }

    private String gerarCodigoAutorizacao() {
        Random random = new Random();
        int numero = 1000000000 + random.nextInt(900000000);
        return String.valueOf(numero);
    }

}
