package br.com.fiap.bean;

import java.time.LocalDate;

public class Inventario {
    private int id;
    private int idUsuario;
    private int idItem;
    private String origem;
    private LocalDate dataAquisicao;

    public Inventario() {
    }

    public Inventario(int id, int idUsuario, int idItem, String origem, LocalDate dataAquisicao) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idItem = idItem;
        this.origem = origem;
        this.dataAquisicao = dataAquisicao;
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

}
