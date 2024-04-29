package org.example.desafiotools.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.desafiotools.enums.StatusTransacao;

import java.time.LocalDateTime;

@Getter
@Setter
public class DescricaoDTO {

    private Double valor;

    private LocalDateTime dataHora;

    private String estabelecimento;

    private String nsu;

    private StatusTransacao status;

    private String codigoAutorizacao;

}
