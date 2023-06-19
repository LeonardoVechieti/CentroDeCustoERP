package com.leonardovechieti.dev.project.repository;

import com.leonardovechieti.dev.project.dao.ModuloConexao;
import com.leonardovechieti.dev.project.model.CentroDeCusto;
import com.leonardovechieti.dev.project.model.Estoque;
import com.leonardovechieti.dev.project.model.Produto;

import java.sql.*;

public class EstoqueRepository {
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    public EstoqueRepository() {
        conexao = ModuloConexao.conector();
    }

    public String lancar(Estoque estoque) {
        String sql = "insert into estoque (idProduto, idMovimentacao, idCentroDeCusto, idOperacao, quantidade, valorUnitario, valorTotal, descricao) values (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, estoque.getIdProduto());
            pst.setInt(2, estoque.getIdMovimentacao());
            pst.setInt(3, estoque.getIdCentroDeCusto());
            pst.setInt(4, estoque.getIdOperacao());
            pst.setString(5, String.valueOf(estoque.getQuantidade()));
            pst.setString(6, String.valueOf(estoque.getValorUnitario()));
            pst.setString(7, String.valueOf(estoque.getValorTotal()));
            pst.setString(8, estoque.getDescricao());
            pst.executeUpdate();
            return "SUCCESS";
        } catch (Exception e) {
            System.out.println(e);
            return "ERROR";
        }
    }

    public String editar(Estoque estoque) {
        String sql = "update estoque set idProduto=?, idMovimentacao=?, idCentroDeCusto=?, quantidade=?, operacao=?, descricao=? where id=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.executeUpdate();
            return "SUCCESS";
        } catch (Exception e) {
            System.out.println(e);
            return "ERROR";
        }
    }

    public String excluir(String id) {
        String sql = "delete from estoque where id=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(id));
            pst.executeUpdate();
            return "SUCCESS";
        } catch (Exception e) {
            System.out.println(e);
            return "ERROR";
        }

    }

    public ResultSet listarAll() {
        //Lista do mais recente para o mais antigo
        String sql = "select id as ID,operacao as OPERACAO, quantidade as QUANTIDADE, idCentroDeCusto as CENTRO from estoque order by id desc";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
        } catch (Exception e) {
            System.out.println(e);
        }
        return rs;
    }

    public Wrapper pesquisar(String nome) {
        String sql = "select id as ID, nome as NOME, descricao as DESCRICAO, inativo as INATIVO from centrodecusto where nome like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, "%" + nome + "%");
            rs = pst.executeQuery();
        } catch (Exception e) {
            System.out.println(e);
        }
        return rs;
    }

    public Estoque buscaId(String id) {
        String sql = "select * from estoque where id = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(id));
            rs = pst.executeQuery();
            if (rs.next()) {
                return new Estoque(rs.getInt("id"), rs.getInt("idProduto"), rs.getInt("idMovimentacao"), rs.getInt("idCentroDeCusto"), rs.getInt("idOperacao"), rs.getBigDecimal("quantidade"), rs.getBigDecimal("valorUnitario"), rs.getBigDecimal("valorTotal"), rs.getString("data"), rs.getString("descricao"));
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    //Retorna o ultimo id cadastrado
    public int ultimoId() {
        String sql = "select max(id) from estoque";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;

    }

    public void fecharConexao() throws SQLException {
        conexao.close();
        System.out.println("Conexao fechada!");
    }

    public ResultSet listarPorProduto(Produto produto) {
        //Lista do mais recente para o mais antigo da uniao das tabelas estoque, produto e centrodecusto
        String sql = "select e.id as ID, o.descricao as OPERACAO, e.quantidade as QUANTIDADE, c.nome as CENTRO, p.descricao as PRODUTO from estoque e\n" +
                "join produto p\n" +
                "on e.idProduto = p.id\n" +
                "join centrodecusto c\n" +
                "on e.idCentroDeCusto = c.id\n" +
                "join operacao o\n" +
                "on e.idOperacao = o.id\n" +
                "where e.idProduto = ? order by e.id desc";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, produto.getId());
            rs = pst.executeQuery();

       } catch (Exception e) {
            System.out.println(e);
        }
        return rs;
    }

    public String retornaTotalEstoque(Produto produto) {
        String sql = "select sum(quantidade) from estoque where idProduto = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, produto.getId());
            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            } else {
                return "0";
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return "0";
    }
}
