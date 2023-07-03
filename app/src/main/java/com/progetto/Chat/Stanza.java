package com.progetto.Chat;

public class Stanza {
    private String nome;
    private int ammi_id;

    public Stanza(String nome, int ammi_id){
        this.nome=nome;
        this.ammi_id=ammi_id;
    }


    public int getAmmi_id() {
        return ammi_id;
    }

    public void setAmmi_id(int ammi_id) {
        this.ammi_id = ammi_id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}

