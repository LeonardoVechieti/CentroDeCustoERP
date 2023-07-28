package com.leonardovechieti.dev.project.model;

import com.leonardovechieti.dev.project.model.enums.TipoOperacao;

public class Operacao {

    private int id;
    private String descricao;
    private Enum<TipoOperacao> operacao;

    private Boolean inativo;

    public Operacao(int id, String descricao, String operacao, boolean inativo) {
        this.id = id;
        this.descricao = descricao;
        this.operacao = TipoOperacao.valueOf(operacao);
        this.inativo = inativo;
    }

    public Operacao(String descricao, String operacao, boolean inativo) {
        this.descricao = descricao;
        this.operacao = TipoOperacao.valueOf(operacao);
        this.inativo = inativo;

    }

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

}
