package com.leonardovechieti.dev.project.model;

import com.leonardovechieti.dev.project.model.enums.Operacao;

public class Movimentacao {
    private int id;
    private int idProduto;
    private int idCentroDeCusto;
    private int quantidade;
    private Number valor;
    private String data;
    private Enum<Operacao> operacao;
    private String descricao;

    public Movimentacao(int id) {
        this.id = id;
    }

    public Movimentacao(int id, int idProduto, int idCentroDeCusto, int quantidade, Number valor, String data, Enum<Operacao> operacao, String descricao) {
        this.id = id;
        this.idProduto = idProduto;
        this.idCentroDeCusto = idCentroDeCusto;
        this.quantidade = quantidade;
        this.valor = valor;
        this.data = data;
        this.operacao = operacao;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public int getIdCentroDeCusto() {
        return idCentroDeCusto;
    }

    public void setIdCentroDeCusto(int idCentroDeCusto) {
        this.idCentroDeCusto = idCentroDeCusto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Number getValor() {
        return valor;
    }

    public void setValor(Number valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Enum<Operacao> getOperacao() {
        return operacao;
    }

    public void setOperacao(Enum<Operacao> operacao) {
        this.operacao = operacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}


