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
        LocalDate dataAtual = LocalDate.now();
        if (!"DISPONIVEL".equalsIgnoreCase(this.status)) {
            throw new MissaoUsuarioException("A missao nao esta disponivel.");
        }
        if (this.dataInicio == null || this.dataFim == null) {
            throw new MissaoUsuarioException("As datas de inicio e fim devem ser informadas.");
        }
        if (dataAtual.isBefore(this.dataInicio) || dataAtual.isAfter(this.dataFim)) {
            this.status = "EXPIRADA";
            throw new MissaoUsuarioException("A missao esta fora do periodo de realizacao.");
        }
        if (this.dataRealizacao != null) {
            throw new MissaoUsuarioException("A missao ja foi concluida.");
        }
    }

    public void concluir() {
        iniciar();
        this.dataRealizacao = LocalDate.now();
    }

}
