package br.com.fiap.dto;
import java.time.LocalDate;

public class MissaoUsuario {
    private int id;
    private int idUsuario;
    private int idMissao;
    private String status;
    private LocalDate dataInicio;

    public MissaoUsuario() {
    }

    public MissaoUsuario(int id, int idUsuario, int idMissao, String status, LocalDate dataInicio) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idMissao = idMissao;
        this.status = status;
        this.dataInicio = dataInicio;
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

    public int getIdMissao() {
        return this.idMissao;
    }

    public void setIdMissao(int idMissao) {
        this.idMissao = idMissao;
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