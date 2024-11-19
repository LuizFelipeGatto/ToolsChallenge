package org.example.desafiotools.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.example.desafiotools.enums.StatusFormaPagamento;
import org.example.desafiotools.util.Constants;

@Getter
@Setter
public class FormaPagamentoDTO {

    @NotBlank(message = Constants.VERIFIQUE_DADOS)
    private String tipo;

    private Integer parcelas;

    @AssertTrue(message = "Parcelas é obrigatória quando o tipo de pagamento não é à vista")
    private boolean isParcelas() {
        return StatusFormaPagamento.AVISTA.name().equalsIgnoreCase(tipo) || parcelas != null && parcelas >= 1;
    }
}

