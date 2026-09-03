package br.com.fiap.bean;

public class Avatar implements Personalizavel {
    private int id;
    private String nome;
    private int idCabelo;
    private int idRoupaCimaInt;
    private int idRoupaCimaExt;
    private int idRoupaBaixo;
    private int idCalcado;
    private int idAcessorio;
    private int idUsuario;

    public Avatar() {
    }

    public Avatar(int id, String nome, int idCabelo, int idRoupaCimaInt,
                  int idRoupaCimaExt, int idRoupaBaixo, int idCalcado,
                  int idAcessorio, int idUsuario) {
        this.id = id;
        this.nome = nome;
        this.idCabelo = idCabelo;
        this.idRoupaCimaInt = idRoupaCimaInt;
        this.idRoupaCimaExt = idRoupaCimaExt;
        this.idRoupaBaixo = idRoupaBaixo;
        this.idCalcado = idCalcado;
        this.idAcessorio = idAcessorio;
        this.idUsuario = idUsuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdCabelo() {
        return idCabelo;
    }

    public void setIdCabelo(int idCabelo) {
        this.idCabelo = idCabelo;
    }

    public int getIdRoupaCimaInt() {
        return idRoupaCimaInt;
    }

    public void setIdRoupaCimaInt(int idRoupaCimaInt) {
        this.idRoupaCimaInt = idRoupaCimaInt;
    }

    public int getIdRoupaCimaExt() {
        return idRoupaCimaExt;
    }

    public void setIdRoupaCimaExt(int idRoupaCimaExt) {
        this.idRoupaCimaExt = idRoupaCimaExt;
    }

    public int getIdRoupaBaixo() {
        return idRoupaBaixo;
    }

    public void setIdRoupaBaixo(int idRoupaBaixo) {
        this.idRoupaBaixo = idRoupaBaixo;
    }

    public int getIdCalcado() {
        return idCalcado;
    }

    public void setIdCalcado(int idCalcado) {
        this.idCalcado = idCalcado;
    }

    public int getIdAcessorio() {
        return idAcessorio;
    }

    public void setIdAcessorio(int idAcessorio) {
        this.idAcessorio = idAcessorio;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public void equiparItem(Item item) {
        if (item == null || item.getModelo() == null) {
            throw new AvatarException("O item e seu modelo devem ser informados.");
        }

        String modelo = item.getModelo();
        if (modelo.equalsIgnoreCase("CABELO")) {
            this.idCabelo = item.getId();
        } else if (modelo.equalsIgnoreCase("ROUPA DE CIMA INTERNA")) {
            this.idRoupaCimaInt = item.getId();
        } else if (modelo.equalsIgnoreCase("ROUPA DE CIMA EXTERNA")) {
            this.idRoupaCimaExt = item.getId();
        } else if (modelo.equalsIgnoreCase("ROUPA DE BAIXO")) {
            this.idRoupaBaixo = item.getId();
        } else if (modelo.equalsIgnoreCase("CALCADO")) {
            this.idCalcado = item.getId();
        } else if (modelo.equalsIgnoreCase("ACESSORIO")) {
            this.idAcessorio = item.getId();
        } else {
            throw new AvatarException("Modelo de item invalido.");
        }
    }

    @Override
    public void removerItem(int posicao) {
        if (posicao == 1) {
            this.idCabelo = 0;
        } else if (posicao == 2) {
            this.idRoupaCimaInt = 0;
        } else if (posicao == 3) {
            this.idRoupaCimaExt = 0;
        } else if (posicao == 4) {
            this.idRoupaBaixo = 0;
        } else if (posicao == 5) {
            this.idCalcado = 0;
        } else if (posicao == 6) {
            this.idAcessorio = 0;
        } else {
            throw new AvatarException("A posicao deve estar entre 1 e 6.");
        }
    }
}
