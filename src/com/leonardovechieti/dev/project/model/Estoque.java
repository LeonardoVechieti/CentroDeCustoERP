package com.leonardovechieti.dev.project.model;

import com.leonardovechieti.dev.project.model.enums.MovimentacaoEstoque;

public class Estoque {
    private int id;
    private int idProduto;
    private int idMovimentacao;
    private int idCentroDeCusto;
    private int quantidade;
    private String data;
    private Enum<MovimentacaoEstoque> movimentacao;
    private String descricao;

}
