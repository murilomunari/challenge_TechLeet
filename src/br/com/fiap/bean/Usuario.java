package br.com.fiap.bean;

public class Usuario {
    private int id;
    private String email;
    private String senha;
    private int pontos;
    private int idAvatar;

    public Usuario() {
    }

    public Usuario(int id, String email, String senha, int pontos, int idAvatar) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.pontos = pontos;
        this.idAvatar = idAvatar;
    }

    public void adicionarPontos(int quantidade) {
        if (quantidade <= 0) {
            throw new UsuarioException("A quantidade de pontos deve ser positiva.");
        }
        this.pontos += quantidade;
    }

    public boolean gastarPontos(int quantidade) {
        if (quantidade <= 0) {
            throw new UsuarioException("A quantidade de pontos deve ser positiva.");
        }
        if (this.pontos < quantidade) {
            return false;
        }
        this.pontos -= quantidade;
        return true;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return this.senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getPontos() {
        return this.pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public int getIdAvatar() {
        return this.idAvatar;
    }

    public void setIdAvatar(int idAvatar) {
        this.idAvatar = idAvatar;
    }
}
