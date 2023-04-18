package com.leonardovechieti.dev.project.model;

import com.leonardovechieti.dev.project.model.enums.Operacao;

public class Estoque {
    private int id;
    private int idProduto;
    private int idMovimentacao;
    private int idCentroDeCusto;
    private String quantidade;
    private String data;
    private Enum<Operacao> operacao;
    private String descricao;
    private String valor;

    private String NomeProduto;

    public Estoque(int id) {
        this.id = id;
    }

    public Estoque(int idProduto, String quantidade, String valor) {
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.valor = valor;
    }

    public Estoque(int id, int idProduto, int idMovimentacao, int idCentroDeCusto, String quantidade, String data, Enum<Operacao> opercao, String descricao) {
        this.id = id;
        this.idProduto = idProduto;
        this.idMovimentacao = idMovimentacao;
        this.idCentroDeCusto = idCentroDeCusto;
        this.quantidade = quantidade;
        this.data = data;
        this.operacao = opercao;
        this.descricao = descricao;
    }

    public Estoque(int id, int idProduto, int idMovimentacao, int idCentroDeCusto, String quantidade, String operacao, String descricao, String valor) {
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

    public Estoque() {

    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
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

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
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

    public String getNomeProduto() {
        return NomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        NomeProduto = nomeProduto;
    }
}
