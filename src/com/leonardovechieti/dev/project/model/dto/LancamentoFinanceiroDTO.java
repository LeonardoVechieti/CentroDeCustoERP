package com.leonardovechieti.dev.project.model.dto;

import com.leonardovechieti.dev.project.model.Estoque;

import java.util.ArrayList;

public class LancamentoFinanceiroDTO {
    private int id;
    private String operacao;
    private String centro;
    private String usuario;
    private String valor;
    private String data;

    private String descricao;

    private ArrayList<EstoqueDTO> estoqueDTO = new ArrayList<>();

    public LancamentoFinanceiroDTO(int id, String operacao, String centro, String usuario, String data, String valor, String descricao) {
        this.id = id;
        this.operacao = operacao;
        this.centro = centro;
        this.usuario = usuario;
        this.data = data;
        this.valor = valor;
        this.descricao = descricao;
    }

    public LancamentoFinanceiroDTO() {

    }


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
}