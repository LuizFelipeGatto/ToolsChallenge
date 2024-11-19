package org.example.desafiotools.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.example.desafiotools.util.Constants;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetalhamentoTransacaoDTO {

    @NotBlank(message = Constants.VERIFIQUE_DADOS)
    @Pattern(regexp = "^[0-9]{19}$", message = "Número do cartão deve conter apenas números e ter entre 19 dígitos")
    private String cartao;

    private Long id;

    @Valid
    @NotNull(message = Constants.VERIFIQUE_DADOS)
    private DescricaoDTO descricao;

    @Valid
    @NotNull(message = Constants.VERIFIQUE_DADOS)
    private FormaPagamentoDTO formaPagamento;

}
