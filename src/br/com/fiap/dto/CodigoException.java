package br.com.fiap.dto;

public class CodigoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CodigoException(String mensagem) {
        super(mensagem);
    }

    public CodigoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
