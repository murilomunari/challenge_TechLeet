package br.com.fiap.dto;

public class Item {
    private int id;
    private String nome;
    private String modelo;
    private int valorPontos;
    private String tipo;

    public Item() {
    }

    public Item(int id, String nome, String modelo, int valorPontos, String tipo) {
        this.id = id;
        this.nome = nome;
        this.modelo = modelo;
        this.valorPontos = valorPontos;
        this.tipo = tipo;
    }

    public boolean verificarCompatibilidade(String tipoDesejado) {
        return this.tipo != null
                && tipoDesejado != null
                && this.tipo.equalsIgnoreCase(tipoDesejado);
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

    public String getModelo() {
        return this.modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getValorPontos() {
        return this.valorPontos;
    }

    public void setValorPontos(int valorPontos) {
        this.valorPontos = valorPontos;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
