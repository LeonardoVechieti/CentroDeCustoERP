package com.leonardovechieti.dev.project.model.enums;

public enum Operacao {
    ENTRADA("Entrada"),
    PRODUCAO("Entrada"),
    VENDA("Saida"),
    BAIXA("Saida"),
    DEVOLUCAO("Saida"),
    TRANSFERENCIA("Transferencia"),
    AJUSTE("Ajuste");


    private String descricao;

    Operacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
