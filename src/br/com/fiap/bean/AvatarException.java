package br.com.fiap.bean;

public class AvatarException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AvatarException(String mensagem) {
        super(mensagem);
    }

    public AvatarException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
