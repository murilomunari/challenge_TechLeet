package br.com.fiap.bean;

import java.time.LocalDate;

public class Log {
    private int id;
    private String assunto;
    private String descricao;
    private LocalDate dataRegistro;
    private int idUsuario;
    private String status;

    public Log() {
    }

    public Log(int id, String assunto, String descricao, LocalDate dataRegistro,
               int idUsuario, String status) {
        this.id = id;
        this.assunto = assunto;
        this.descricao = descricao;
        this.dataRegistro = dataRegistro;
        this.idUsuario = idUsuario;
        this.status = status;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAssunto() {
        return this.assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataRegistro() {
        return this.dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public int getIdUsuario() {
        return this.idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
