package com.leonardovechieti.dev.project.model;

public class Produto {
    private int id;
    private String descricao;
    private String preco;
    private String unidade;
    private Boolean inativo;
    private Boolean servico;
    private Boolean estoque;
    private Boolean producao;
    private String dataCriacao;
    private String dataModificacao;
    private int usuario;



    public Produto(int id, String descricao, String preco, String unidade, Boolean inativo, Boolean servico, Boolean estoque, Boolean producao, String dataModificacao, int usuario) {
        this.id = id;
        this.descricao = descricao;
        this.preco = preco;
        this.unidade = unidade;
        this.inativo = inativo;
        this.servico = servico;
        this.estoque = estoque;
        this.producao = producao;
        this.dataModificacao = dataModificacao;
        this.usuario = usuario;
    }

    public Produto() {

    }
    public Produto(int id){
        this.id = id;
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

    public String getPreco() {
        return String.valueOf(preco);
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public Boolean getInativo() {
        return inativo;
    }

    public void setInativo(Boolean inativo) {
        this.inativo = inativo;
    }

    public Boolean getServico() {
        return servico;
    }

    public void setServico(Boolean servico) {
        this.servico = servico;
    }

    public Boolean getEstoque() {
        return estoque;
    }

    public void setEstoque(Boolean estoque) {
        this.estoque = estoque;
    }

    public Boolean getProducao() {
        return producao;
    }

    public void setProducao(Boolean producao) {
        this.producao = producao;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getDataModificacao() {
        return dataModificacao;
    }

    public void setDataModificacao(String dataModificacao) {
        this.dataModificacao = dataModificacao;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }


}

