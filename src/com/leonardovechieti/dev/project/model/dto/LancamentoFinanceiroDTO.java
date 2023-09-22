package com.leonardovechieti.dev.project.model.dto;

public class LancamentoFinanceiroDTO {
    private int id;
    private String operacao;
    private String centro;
    private String usuario;
    private String data;
    private String valor;

    public LancamentoFinanceiroDTO(int id, String operacao, String centro, String usuario, String data, String valor) {
        this.id = id;
        this.operacao = operacao;
        this.centro = centro;
        this.usuario = usuario;
        this.data = data;
        this.valor = valor;
    }

    public LancamentoFinanceiroDTO() {

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
}