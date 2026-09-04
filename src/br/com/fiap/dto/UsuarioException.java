package br.com.fiap.dto;

public class UsuarioException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UsuarioException(String mensagem) {
        super(mensagem);
    }

    public UsuarioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
