package com.leonardovechieti.dev.project.model;

public class Empresa {
    private String nome;
    private String razao;
    private String cnpj;

    public Empresa(String nome, String razao, String cnpj) {
        this.nome = nome;
        this.razao = razao;
        this.cnpj = cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRazao() {
        return razao;
    }

    public void setRazao(String razao) {
        this.razao = razao;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}
