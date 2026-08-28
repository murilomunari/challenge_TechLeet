package br.com.fiap.dto;

public class Avatar implements Personalizavel {
    private int id;
    private String nome;
    private Item roupa;
    private Item chapeu;
    private Item oculos;

    public Avatar() {
    }

    public Avatar(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.roupa = null;
        this.chapeu = null;
        this.oculos = null;
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

    public Item getRoupa() {
        return this.roupa;
    }

    public void setRoupa(Item roupa) {
        this.roupa = roupa;
    }

    public Item getChapeu() {
        return this.chapeu;
    }

    public void setChapeu(Item chapeu) {
        this.chapeu = chapeu;
    }

    public Item getOculos() {
        return this.oculos;
    }

    public void setOculos(Item oculos) {
        this.oculos = oculos;
    }

    @Override
    public void equiparItem(Item item) {
        if (item == null) {
            return;
        }

        String tipo = item.getTipo();

        if (tipo.equals("roupa")) {
            this.roupa = item;
        } else if (tipo.equals("chapeu")) {
            this.chapeu = item;
        } else if (tipo.equals("oculos")) {
            this.oculos = item;
        }
    }

    @Override
    public void removerItem(int posicao) {
        if (posicao == 1) {
            this.roupa = null;
        } else if (posicao == 2) {
            this.chapeu = null;
        } else if (posicao == 3) {
            this.oculos = null;
        }
    }
}
