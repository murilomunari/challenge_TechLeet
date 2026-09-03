package br.com.fiap.bean;

public class MissaoUsuarioException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MissaoUsuarioException(String mensagem) {
        super(mensagem);
    }

    public MissaoUsuarioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
