package br.com.fiap.dto;

import java.time.LocalDate;

public class Codigo {
    private int id;
    private String codigoResgate;
    private LocalDate dataValidade;
    private String status;
    private LocalDate dataResgate;
    private int idParceria;

    public Codigo() {
    }

    public Codigo(int id, String codigoResgate, LocalDate dataValidade, String status, int idParceria) {
        this.id = id;
        this.codigoResgate = codigoResgate;
        this.dataValidade = dataValidade;
        this.status = status;
        this.dataResgate = null;
        this.idParceria = idParceria;
    }


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigoResgate() {
        return this.codigoResgate;
    }

    public void setCodigoResgate(String codigoResgate) {
        this.codigoResgate = codigoResgate;
    }

    public LocalDate getDataValidade() {
        return this.dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDataResgate() {
        return this.dataResgate;
    }

    public void setDataResgate(LocalDate dataResgate) {
        this.dataResgate = dataResgate;
    }

    public int getIdParceria() {
        return this.idParceria;
    }

    public void setIdParceria(int idParceria) {
        this.idParceria = idParceria;
    }

    public boolean validarResgate() {
        return this.status.equals("ativo");
    }
}
