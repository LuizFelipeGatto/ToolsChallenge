package org.example.desafiotools.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.desafiotools.util.Constants;

@Getter
@Setter
@NoArgsConstructor
public class PagamentoDTO {

    @Valid
    @NotNull(message = Constants.VERIFIQUE_DADOS)
    private DetalhamentoTransacaoDTO transacao;
}
