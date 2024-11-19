package org.example.desafiotools.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ErroDTO {

    private String campo;
    private String mensagem;

}
