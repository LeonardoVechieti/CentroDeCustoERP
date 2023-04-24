package com.leonardovechieti.dev.project.model;

import com.leonardovechieti.dev.project.model.enums.TipoOperacao;

public class Movimentacao {
    private int id;
    private int idCentroDeCusto;

    private int idOperacao;

    private Number valorTotal;
    private String data;
    private String descricao;

    public Movimentacao(int id) {
        this.id = id;
    }

    public Movimentacao(int id, int idCentroDeCusto, int idOperacao, Number valorTotal, String data, String descricao) {
        this.id = id;
        this.idCentroDeCusto = idCentroDeCusto;
        this.idOperacao = idOperacao;
        this.valorTotal = valorTotal;
        this.data = data;
        this.descricao = descricao;
    }

    public Movimentacao() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCentroDeCusto() {
        return idCentroDeCusto;
    }

    public void setIdCentroDeCusto(int idCentroDeCusto) {
        this.idCentroDeCusto = idCentroDeCusto;
    }

    public int getIdOperacao() {
        return idOperacao;
    }

    public void setIdOperacao(int idOperacao) {
        this.idOperacao = idOperacao;
    }

    public Number getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Number valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}


