package org.example.desafiotools.util;

import org.example.desafiotools.enums.StatusFormaPagamento;

import java.util.Arrays;
import java.util.List;

public class Constants {

    public static final String FORMA_PAGAMENTO_INVALIDA = "Forma de pagamento inválida.";

    public static final String DIGITOS_EXCESSO = "Dígitos de cartão a mais do que o necessário.";

    public static final String DIGITOS_FALTANTES = "Faltando dígitos de cartão.";

    public static final String DIGITOS_LETRAS = "Dígitos de cartão precisam ser somente números.";

    public static final String TRANSACAO_NAO_ENCONTRADA = "Transação não encontrada.";

    public static final String ESTORNO_REALIZADO_ANTES = "O Estorno já foi realizado.";

    public static final String NUMERO_FALTANTE = "Não encontramos o número do cartão.";

    public static final String VERIFIQUE_DADOS = "Verifique os dados enviados.";

    public static List<String> getTiposFormaPagamento() {
        return Arrays.asList(StatusFormaPagamento.AVISTA.name(), StatusFormaPagamento.PARCELADO_LOJA.name(), StatusFormaPagamento.PARCELADO_EMISSOR.name());
    }
}
