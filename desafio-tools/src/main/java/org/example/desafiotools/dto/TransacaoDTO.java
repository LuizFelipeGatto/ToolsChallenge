package org.example.desafiotools.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransacaoDTO {

    private String cartao;

    private Long id;

    private DescricaoDTO descricao;

    private FormaPagamentoDTO formaPagamento;

}
