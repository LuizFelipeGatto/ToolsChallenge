package org.example.desafiotools.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class MensagemErro {

    private String mensagem;

    public MensagemErro(String mensagem) {
        this.mensagem = mensagem;
    }
}
