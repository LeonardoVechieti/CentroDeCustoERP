package com.leonardovechieti.dev.project.model;

public class LancamentoFinanceiro {
    private int id;
    private int idCentroDeCusto;

    private int idOperacao;

    private String valorTotal;
    private String data;
    private String descricao;
    private int usuario;

    public LancamentoFinanceiro(int id) {
        this.id = id;
    }

    public LancamentoFinanceiro(int id, int idCentroDeCusto, int idOperacao, String valorTotal, String data, String descricao) {
        this.id = id;
        this.idCentroDeCusto = idCentroDeCusto;
        this.idOperacao = idOperacao;
        this.valorTotal = valorTotal;
        this.data = data;
        this.descricao = descricao;
    }

    public LancamentoFinanceiro() {}

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

    public String getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(String valorTotal) {
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

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }
}


