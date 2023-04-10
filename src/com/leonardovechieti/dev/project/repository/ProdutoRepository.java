package com.leonardovechieti.dev.project.repository;

import com.leonardovechieti.dev.project.dao.ModuloConexao;
import com.leonardovechieti.dev.project.message.Message;
import com.leonardovechieti.dev.project.model.Produto;
import com.leonardovechieti.dev.project.views.MessageView;

import java.sql.*;
import java.util.List;

public class ProdutoRepository {
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public ProdutoRepository() {
        conexao = ModuloConexao.conector();
    }

    public void salvar(String descricao, String preco, String unidade, Boolean ativo, Boolean servico, Boolean estoque, Boolean producao, int usuario) {
        String sql = "insert into produto (descricao, preco, unidade, ativo, servico, estoque, producao, usuario) values (?,?,?,?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, descricao);
            pst.setString(2, preco);
            pst.setString(3, unidade);
            pst.setBoolean(4, ativo);
            pst.setBoolean(5, servico);
            pst.setBoolean(6, estoque);
            pst.setBoolean(7, producao);
            pst.setInt(8, usuario);
            pst.executeUpdate();
            Message message = new Message("Sucesso!", "Produto cadastrado com sucesso!", "success");
        } catch (Exception e) {
            //System.out.println(e);
            Message message = new Message("Erro!", "Erro ao cadastrar produto!" + e, "error");
        }
    }

    public void editar(String nome, String descricao, String preco, String quantidade, String id) {
        String sql = "update produto set nome=?, descricao=?, preco=?, quantidade=? where id=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, nome);
            pst.setString(2, descricao);
            pst.setString(3, preco);
            pst.setString(4, quantidade);
            pst.setString(5, id);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void excluir(String id) {
        String sql = "delete from produto where id=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public ResultSet listarAll() {
        String sql = "select * from produto";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
        } catch (Exception e) {
            System.out.println(e);
        }
        return rs;
    }

    public Wrapper pesquisar(String nome) {
        String sql = "select id as ID, descricao as PRODUTO, unidade as UNIDADE, preco as PREÇO  from produto where descricao like ?";;
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, nome + "%");
            rs = pst.executeQuery();
        } catch (Exception e) {
            System.out.println(e);
        }
        return rs;
    }
}
