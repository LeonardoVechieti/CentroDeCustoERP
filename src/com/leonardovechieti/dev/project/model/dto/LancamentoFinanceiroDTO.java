package com.leonardovechieti.dev.project.model.dto;

import java.util.ArrayList;

public class LancamentoFinanceiroDTO {
    private int id;
    private String operacao;
    private String tipoOperacao;

    private String receita;
    private String centro;
    private String usuario;
    private String valor;
    private String desconto;
    private String descontoTipo;
    private String data;
    private String descricao;
    private Boolean cancelado;
    private int idLancamentoAnexo;
    private ArrayList<EstoqueDTO> estoqueDTO = new ArrayList<>();
    private ReportDTO report;

    public LancamentoFinanceiroDTO() { }

    public void addArrayEstoque(ArrayList<EstoqueDTO> estoqueDTO) {
        this.estoqueDTO = estoqueDTO;
    }

    public ArrayList<EstoqueDTO> getEstoque() {
        return estoqueDTO;
    }

    public void setEstoque(ArrayList<EstoqueDTO> estoqueDTO) {
        this.estoqueDTO = estoqueDTO;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public String getCentro() {
        return centro;
    }

    public void setCentro(String centro) {
        this.centro = centro;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getCancelado() {
        return cancelado;
    }

    public void setCancelado(Boolean cancelado) {
        this.cancelado = cancelado;
    }

    public int getIdLancamentoAnexo() {
        return idLancamentoAnexo;
    }

    public void setIdLancamentoAnexo(int idLancamentoAnexo) {
        this.idLancamentoAnexo = idLancamentoAnexo;
    }

    public String getDesconto() {
        return desconto;
    }

    public void setDesconto(String desconto) {
        this.desconto = desconto;
    }

    public String getDescontoTipo() {
        return descontoTipo;
    }

    public void setDescontoTipo(String descontoTipo) {
        this.descontoTipo = descontoTipo;
    }

    public ReportDTO getReport() { return report; }

    public void setReport(ReportDTO report) { this.report = report; }

    public String getTipoOperacao() { return tipoOperacao; }

    public void setTipoOperacao(String tipoOperacao) { this.tipoOperacao = tipoOperacao; }

    public String getReceita() { return receita; }

    public void setReceita(String receita) { this.receita = receita; }
}