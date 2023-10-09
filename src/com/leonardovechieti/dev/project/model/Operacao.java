package com.leonardovechieti.dev.project.model;

import com.leonardovechieti.dev.project.model.enums.TipoOperacao;
import com.leonardovechieti.dev.project.model.enums.TipoReceita;

public class Operacao {

    private int id;
    private String descricao;
    private Enum<TipoOperacao> operacao;
    private Enum<TipoReceita> receita;
    private Boolean inativo;
    private Boolean movimentaEstoque;

    public Operacao(int id, String descricao, String operacao, String receita, boolean inativo, boolean movimentaEstoque) {
        this.id = id;
        this.descricao = descricao;
        this.operacao = TipoOperacao.valueOf(operacao);
        this.receita = TipoReceita.valueOf(receita);
        this.inativo = inativo;
        this.movimentaEstoque = movimentaEstoque;
    }

    public Operacao(String descricao, String operacao, String receita, boolean inativo, boolean movimentaEstoque) {
        this.descricao = descricao;
        this.operacao = TipoOperacao.valueOf(operacao);
        this.receita = TipoReceita.valueOf(receita);
        this.inativo = inativo;
        this.movimentaEstoque = movimentaEstoque;

    }
    public Operacao() { }

    public Boolean getInativo() {
        return inativo;
    }

    public void setInativo(Boolean inativo) {
        this.inativo = inativo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Enum<TipoOperacao> getOperacao() {
        return operacao;
    }

    public void setOperacao(Enum<TipoOperacao> operacao) {
        this.operacao = operacao;
    }

    public Enum<TipoReceita> getReceita() {
        return receita;
    }

    public void setReceita(Enum<TipoReceita> receita) {
        this.receita = receita;
    }

    public Boolean getMovimentaEstoque() {
        return movimentaEstoque;
    }
    public void setMovimentaEstoque(Boolean movimentaEstoque) {
        this.movimentaEstoque = movimentaEstoque;
    }
}
