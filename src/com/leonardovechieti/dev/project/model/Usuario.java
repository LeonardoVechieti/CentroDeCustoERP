package com.leonardovechieti.dev.project.model;

public class Usuario {
    private int id;
    private String nome;
    private String login;
    private String senha;
    private Boolean inativo;
    private String perfil;

    public Usuario(int id, String nome, String login, String senha, Boolean inativo, String perfil) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.inativo = inativo;
        this.perfil = perfil;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean getInativo() {
        return inativo;
    }

    public void setInativo(Boolean inativo) {
        this.inativo = inativo;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }
}
