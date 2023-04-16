package com.leonardovechieti.dev.project.model;

public class CentroDeCusto {

    private int id;
    private String nome;
    private String descricao;
    private Boolean inativo;

    public CentroDeCusto(int id, String nome, String descricao, Boolean inativo) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.inativo = inativo;
    }

    public CentroDeCusto() {}

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getInativo() {
        return inativo;
    }

    public void setInativo(Boolean inativo) {
        this.inativo = inativo;
    }
}
