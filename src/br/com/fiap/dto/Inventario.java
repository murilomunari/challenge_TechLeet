package br.com.fiap.dto;

import java.time.LocalDate;

public class Inventario {
    private int id;
    private int idUsuario;
    private int idItem;
    private String origem;
    private LocalDate dataAquisicao;
    private int quantidade;

    public Inventario() {
    }

    public Inventario(int id, int idUsuario, int idItem, String origem, LocalDate dataAquisicao, int quantidade) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idItem = idItem;
        this.origem = origem;
        this.dataAquisicao = dataAquisicao;
        this.quantidade = quantidade;
    }


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return this.idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdItem() {
        return this.idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public String getOrigem() {
        return this.origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public LocalDate getDataAquisicao() {
        return this.dataAquisicao;
    }

    public void setDataAquisicao(LocalDate dataAquisicao) {
        this.dataAquisicao = dataAquisicao;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
