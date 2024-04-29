package org.example.desafiotools.service;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.desafiotools.dto.DescricaoDTO;
import org.example.desafiotools.dto.FormaPagamentoDTO;
import org.example.desafiotools.dto.TransacaoDTO;
import org.example.desafiotools.enums.StatusTransacao;
import org.example.desafiotools.model.Transacao;
import org.example.desafiotools.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class TransacaoService {

    @Autowired
    private final TransacaoRepository transacaoRepository;

    @Transactional
    public Transacao saveTransacao(Transacao transacao) {
        return transacaoRepository.save(transacao);
    }

    public List<Transacao> listTransacoes() {
        return transacaoRepository.findAll();
    }

    public Transacao buscaTransacaoPorId(Long id) {
        return transacaoRepository.findById(id).orElse(null);
    }

    public String numeroCartaoCriptografado (String numeroCartao) {
        int numero = Integer.parseInt(numeroCartao);

        String numeroString = String.valueOf(numero);

        int[] digitos =  new int[numeroString.length()];

        for (int i = 0; i < numeroString.length(); i++) {
            digitos[i] = Integer.parseInt(String.valueOf(numeroString.charAt(i)));
        }

        List<Integer> numerosMostrados = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            numerosMostrados.add(digitos[i]);
        }

        // Adicionando os números substituídos por *
        for (int i = 2; i < digitos.length - 2; i++) {
            numerosMostrados.add(-1); // Adicionando um marcador para ser substituído por *
        }

        // Adicionando os dois últimos números
        for (int i = digitos.length - 2; i < digitos.length; i++) {
            numerosMostrados.add(digitos[i]);
        }

        // Juntando os números em uma única string
        StringBuilder resultado = new StringBuilder();
        for (int num : numerosMostrados) {
            if (num == -1) {
                resultado.append("* ");
            } else {
                resultado.append(num).append(" ");
            }
        }

        System.out.println(digitos);

        return resultado.toString();
    }

    @Transactional
    public TransacaoDTO estornoTransacao(Transacao transacao) {
        Transacao transacaoBuscada = transacaoRepository.findById(transacao.getId()).orElse(null);
        transacaoBuscada.setStatus(StatusTransacao.CANCELADO);
        transacaoRepository.save(transacaoBuscada);

        DescricaoDTO descricaoDTO = new DescricaoDTO();
        descricaoDTO.setCodigoAutorizacao(transacaoBuscada.getCodigoAutorizacao());
        descricaoDTO.setStatus(transacaoBuscada.getStatus());
        descricaoDTO.setNsu(transacaoBuscada.getNsu());
        descricaoDTO.setValor(transacaoBuscada.getValor());
        descricaoDTO.setEstabelecimento(transacaoBuscada.getEstabelecimento());
        descricaoDTO.setDataHora(transacaoBuscada.getDataHora());

        FormaPagamentoDTO formaPagamentoDTO = new FormaPagamentoDTO();
        formaPagamentoDTO.setTipoPagamento(transacaoBuscada.getTipoPagamento());
        formaPagamentoDTO.setParcelas(transacaoBuscada.getParcelas());

//        Gson gson = new Gson();
//        String json = gson.toJson(TransacaoDTO
//                .builder()
//                .id(transacaoBuscada.getId())
//                .cartao(transacaoBuscada.getCartao())
//                .descricao(descricaoDTO)
//                .formaPagamento(formaPagamentoDTO)
//                .build());

        return TransacaoDTO
                .builder()
                .id(transacaoBuscada.getId())
                .cartao(numeroCartaoCriptografado(transacaoBuscada.getCartao()))
                .descricao(descricaoDTO)
                .formaPagamento(formaPagamentoDTO)
                .build();
    }

}
