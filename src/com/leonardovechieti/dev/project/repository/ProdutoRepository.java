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
        String sql = "insert into produto (descricao, preco, unidade, ativo, servico, estoque, producao, dataModificacao, usuario) values (?,?,?,?,?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, descricao);
            pst.setString(2, preco);
            pst.setString(3, unidade);
            pst.setBoolean(4, ativo);
            pst.setBoolean(5, servico);
            pst.setBoolean(6, estoque);
            pst.setBoolean(7, producao);
            pst.setDate(8, new Date(System.currentTimeMillis()));
            pst.setInt(9, usuario);
            pst.executeUpdate();
            Integer idCriado = ultimoId();
            Message message = new Message("Sucesso!", "Produto cadastrado com sucesso!" + "ID: " +idCriado, "success");
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
            //System.out.println(e);
            Message message = new Message("Alerta!", "Produto já possui movimentação, não é possível excluir, mas pode ser inativado a qualquer momento!" , "alert");
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
            pst.setString(1, "%" + nome + "%");
            rs = pst.executeQuery();
        } catch (Exception e) {
            System.out.println(e);
        }
        return rs;
    }

    public Produto buscaId(String id) {
        String sql = "select * from produto where id= ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt(1));
                produto.setDescricao(rs.getString(2));
                produto.setPreco(rs.getDouble(3));
                produto.setUnidade(rs.getString(4));
                produto.setAtivo(rs.getBoolean(5));
                produto.setServico(rs.getBoolean(6));
                produto.setEstoque(rs.getBoolean(7));
                produto.setProducao(rs.getBoolean(8));
                produto.setDataCriacao(rs.getString(9));
                produto.setDataModificacao(rs.getString(10));
                produto.setUsuario(rs.getInt(11));
                return produto;
            }
        } catch (Exception e) {
            Message message = new Message("Erro!", "Erro ao buscar produto!" + e, "error");
        }
        return null;
    }

    //Retorna o ultimo id cadastrado
    public int ultimoId() {
        String sql = "select max(id) from produto";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }
}
