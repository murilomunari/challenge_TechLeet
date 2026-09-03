package br.com.fiap.bean;

public class AvatarException extends RuntimeException {
    public AvatarException(String mensagem) {
        super(mensagem);
    }

    public AvatarException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
