package br.com.fiap.bean;

import java.time.LocalDate;

public class Codigo {
    private int id;
    private String codigoResgate;
    private LocalDate dataValidade;
    private String status;
    private LocalDate dataResgate;
    private int idItem;
    private int idParceria;

    public Codigo() {
    }

    public Codigo(int id, String codigoResgate, String status, LocalDate dataValidade,
                  LocalDate dataResgate, int idItem, int idParceria) {
        this.id = id;
        this.codigoResgate = codigoResgate;
        this.status = status;
        this.dataValidade = dataValidade;
        this.dataResgate = dataResgate;
        this.idItem = idItem;
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

    public int getIdItem() {
        return this.idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public int getIdParceria() {
        return this.idParceria;
    }

    public void setIdParceria(int idParceria) {
        this.idParceria = idParceria;
    }

    public boolean validarResgate() {
        return validarResgate(LocalDate.now());
    }

    public boolean validarResgate(LocalDate dataAtual) {
        if (dataAtual == null) {
            throw new CodigoException("A data atual deve ser informada.");
        }
        return "DISPONIVEL".equalsIgnoreCase(this.status)
                && this.dataResgate == null
                && this.dataValidade != null
                && !dataAtual.isAfter(this.dataValidade);
    }

    public boolean resgatar(LocalDate dataResgate) {
        if (!validarResgate(dataResgate)) {
            return false;
        }
        this.dataResgate = dataResgate;
        this.status = "RESGATADO";
        return true;
    }

    public boolean resgatar() {
        return resgatar(LocalDate.now());
    }
}
