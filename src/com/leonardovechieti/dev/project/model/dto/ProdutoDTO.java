package com.leonardovechieti.dev.project.model.dto;

public class ProdutoDTO {
    private int id;
    private String descricao;
    private String preco;
    private String saldoEstoque;

    private String valorSaldoTotal;
    private String unidade;
    private Boolean inativo;
    private Boolean servico;
    private Boolean estoque;
    private Boolean producao;
    private String dataCriacao;
    private String dataModificacao;
    private int usuario;
    private String nomeUsuario;
    private ReportDTO report;
    public ProdutoDTO() {}

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

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public ReportDTO getReport() {
        return report;
    }

    public void setReport(ReportDTO report) {
        this.report = report;
    }

    public String getSaldoEstoque() {
        return saldoEstoque;
    }
    public void setSaldoEstoque(String saldoEstoque) {
        this.saldoEstoque = saldoEstoque;
    }
    public String getValorSaldoTotal() {
        return valorSaldoTotal;
    }
    public void setValorSaldoTotal(String valorSaldoTotal) {
        this.valorSaldoTotal = valorSaldoTotal;
    }
}

