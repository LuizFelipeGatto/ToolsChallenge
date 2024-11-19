package org.example.desafiotools.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.desafiotools.util.Constants;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransacaoDTO {

    @NotNull(message = Constants.VERIFIQUE_DADOS)
    private DetalhamentoTransacaoDTO transacao;

}
