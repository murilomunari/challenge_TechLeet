package br.com.fiap.bean;
import java.time.LocalDate;

public class MissaoUsuario {
    private int id;
    private int idUsuario;
    private int idMissao;
    private String status;
    private LocalDate dataRealizacao;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public MissaoUsuario() {
    }

    public MissaoUsuario(int id, int idUsuario, int idMissao, String status,
                         LocalDate dataRealizacao, LocalDate dataInicio, LocalDate dataFim) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idMissao = idMissao;
        this.status = status;
        this.dataRealizacao = dataRealizacao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
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

    public LocalDate getDataRealizacao() {
        return this.dataRealizacao;
    }

    public void setDataRealizacao(LocalDate dataRealizacao) {
        this.dataRealizacao = dataRealizacao;
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

    public void iniciar() {
        if (this.status != null && !this.status.equalsIgnoreCase("pendente")) {
            throw new MissaoUsuarioException("A missao ja foi iniciada ou finalizada.");
        }
        this.status = "andamento";
        this.dataInicio = LocalDate.now();
    }

    public void concluir() {
        if (!"andamento".equalsIgnoreCase(this.status)) {
            throw new MissaoUsuarioException("Somente uma missao em andamento pode ser concluida.");
        }
        this.status = "concluida";
        this.dataRealizacao = LocalDate.now();
    }

}
