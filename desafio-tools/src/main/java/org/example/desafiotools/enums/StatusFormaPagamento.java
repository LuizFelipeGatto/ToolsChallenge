package org.example.desafiotools.enums;

import java.util.Arrays;

public enum StatusFormaPagamento {

    AVISTA,
    PARCELADO_LOJA,
    PARCELADO_EMISSOR,
    INVALIDO;

    public static StatusFormaPagamento fromString(String tipoPagamento) {
        return Arrays.stream(StatusFormaPagamento.values())
                .filter(status -> status.name().equalsIgnoreCase(tipoPagamento))
                .findFirst()
                .orElse(INVALIDO);
    }
}
