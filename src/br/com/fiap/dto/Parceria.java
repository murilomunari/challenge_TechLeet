package br.com.fiap.dto;

import java.time.LocalDate;

public class Parceria {
    private int id;
    private String nome;
    private String tipo;
    private String status;
    private LocalDate dataInicio;

    public Parceria() {
    }

    public Parceria(int id, String nome, String tipo, String status, LocalDate dataInicio) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.status = status;
        this.dataInicio = dataInicio;
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

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDataInicio() {
        return this.dataInicio;
    }
}
