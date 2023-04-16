package com.leonardovechieti.dev.project.model.enums;

public enum MovimentacaoEstoque {
    ENTRADA("Entrada"),
    PRODUCAO("Entrada"),
    VENDA("Saida"),
    BAIXA("Saida"),
    DEVOLUCAO("Saida"),
    TRANSFERENCIA("Transferencia"),
    AJUSTE("Ajuste");


    private String descricao;

    MovimentacaoEstoque(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
