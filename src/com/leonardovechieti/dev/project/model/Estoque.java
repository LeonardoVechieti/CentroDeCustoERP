package com.leonardovechieti.dev.project.model;

import com.leonardovechieti.dev.project.model.enums.Operacao;

public class Estoque {
    private int id;
    private int idProduto;
    private int idMovimentacao;
    private int idCentroDeCusto;
    private int quantidade;
    private String data;
    private Enum<Operacao> operacao;
    private String descricao;
    private Number valor;

    public Estoque(int id) {
        this.id = id;
    }

    public Estoque(String idProduto, String quantidade, String valor) {
        this.idProduto = Integer.parseInt(idProduto);
        this.quantidade = Integer.parseInt(quantidade);
        this.valor = Double.parseDouble(valor);
    }

    public Estoque(int id, int idProduto, int idMovimentacao, int idCentroDeCusto, int quantidade, String data, Enum<Operacao> opercao, String descricao) {
        this.id = id;
        this.idProduto = idProduto;
        this.idMovimentacao = idMovimentacao;
        this.idCentroDeCusto = idCentroDeCusto;
        this.quantidade = quantidade;
        this.data = data;
        this.operacao = opercao;
        this.descricao = descricao;
    }

    public Estoque(int id, int idProduto, int idMovimentacao, int idCentroDeCusto, int quantidade, String operacao, String descricao, Number valor) {
        this.id = id;
        this.idProduto = idProduto;
        this.idMovimentacao = idMovimentacao;
        this.idCentroDeCusto = idCentroDeCusto;
        this.quantidade = quantidade;
        this.data = data;
        //Convertendo String para Enum
        this.operacao = Operacao.valueOf(operacao);
        this.descricao = descricao;
        this.valor = valor;
    }

    public Number getValor() {
        return valor;
    }

    public void setValor(Number valor) {
        this.valor = valor;
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

    public int getIdMovimentacao() {
        return idMovimentacao;
    }

    public void setIdMovimentacao(int idMovimentacao) {
        this.idMovimentacao = idMovimentacao;
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
