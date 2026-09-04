package br.com.fiap.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Parceria {
    private int id;
    private String nome;
    private String tipo;
    private String status;
    private BigDecimal custoMensal;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public Parceria() {
    }

    public Parceria(int id, String nome, String tipo, String status, BigDecimal custoMensal,
                    LocalDate dataInicio, LocalDate dataFim) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.status = status;
        this.custoMensal = custoMensal;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
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

    public BigDecimal getCustoMensal() {
        return this.custoMensal;
    }

    public void setCustoMensal(BigDecimal custoMensal) {
        this.custoMensal = custoMensal;
    }

    public LocalDate getDataInicio() {
        return this.dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return this.dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }
}
