package com.leonardovechieti.dev.project.model;

import com.leonardovechieti.dev.project.model.enums.TipoOperacao;

public class Estoque {
    private int id;
    private int idProduto;
    private int idLancamentoFinanceiro;
    private int idCentroDeCusto;
    private int idOperacao;
    private Number quantidade;
    private Number valorUnitario;
    private Number valorTotal;
    private String data;
    private String descricao;
    //Campos abaixo não são persistidos no banco de dados
    private String descricaoProduto;


    public Estoque(int id, int idProduto, int idLancamentoFinanceiro, int idCentroDeCusto, int idOperacao, Number quantidade, Number valorUnitario, Number valorTotal, String data, String descricao) {
        this.id = id;
        this.idProduto = idProduto;
        this.idLancamentoFinanceiro = idLancamentoFinanceiro;
        this.idCentroDeCusto = idCentroDeCusto;
        this.idOperacao = idOperacao;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.valorTotal = valorTotal;
        this.data = data;
        this.descricao = descricao;
    }

    public Estoque() {}

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

    public int getIdLancamentoFinanceiro() {
        return idLancamentoFinanceiro;
    }

    public void setIdLancamentoFinanceiro(int idMovimentacao) {
        this.idLancamentoFinanceiro = idMovimentacao;
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

    public Number getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Number quantidade) {
        this.quantidade = quantidade;
    }

    public Number getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(Number valorUnitario) {
        this.valorUnitario = valorUnitario;
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

    public String getDescricaoProduto() {
        return descricaoProduto;
    }

    public void setDescricaoProduto(String descricaoProduto) {
        this.descricaoProduto = descricaoProduto;
    }
}
