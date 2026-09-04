package br.com.fiap.dto;

import java.time.LocalDate;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private String senha;
    private LocalDate dataNascimento;
    private int pontos;

    public Usuario() {
    }

    public Usuario(int id, String nome, String email, String senha,
                   LocalDate dataNascimento, int pontos) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.pontos = pontos;
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

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public LocalDate getDataNascimento() {
        return this.dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public int getPontos() {
        return this.pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

}
