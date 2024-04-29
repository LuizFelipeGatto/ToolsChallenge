package org.example.desafiotools.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.desafiotools.enums.StatusFormaPagamento;

@Getter
@Setter
public class FormaPagamentoDTO {

    private StatusFormaPagamento tipoPagamento;

    private Integer parcelas;
}
