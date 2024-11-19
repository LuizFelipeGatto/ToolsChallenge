package org.example.desafiotools.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.desafiotools.util.Constants;

@Getter
@Setter
public class DescricaoDTO {

    @NotNull(message = Constants.VERIFIQUE_DADOS)
    private Double valor;

    private String dataHora;

    @NotBlank(message = Constants.VERIFIQUE_DADOS)
    private String estabelecimento;

    private String nsu;

    private String codigoAutorizacao;

    private String status;

}
