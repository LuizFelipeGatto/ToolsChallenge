package org.example.desafiotools.util;

public class Error {

    public static void lancaErro(String mensagem, Long id) {
        throw new BadRequestException(mensagem.replace("<id>", id.toString()));
    }

    public static void lancaErro(String mensagem) {
        throw new BadRequestException(mensagem);
    }
}
