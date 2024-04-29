package org.example.desafiotools.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TransacaoDTO {

    private Long id;

    private String cartao;

    private DescricaoDTO descricao;

    private FormaPagamentoDTO formaPagamento;

}
