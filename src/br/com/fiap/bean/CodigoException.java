package br.com.fiap.bean;

public class CodigoException extends RuntimeException {
    public CodigoException(String mensagem) {
        super(mensagem);
    }

    public CodigoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
