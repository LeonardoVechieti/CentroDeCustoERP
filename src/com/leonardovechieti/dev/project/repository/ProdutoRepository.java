package com.leonardovechieti.dev.project.repository;

import com.leonardovechieti.dev.project.dao.ModuloConexao;
import com.leonardovechieti.dev.project.util.Message;
import com.leonardovechieti.dev.project.model.Produto;
import com.leonardovechieti.dev.project.views.MessageView;

import java.sql.*;

public class ProdutoRepository {
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public ProdutoRepository() {
        conexao = ModuloConexao.conector();
    }

    public String salvar(String descricao, String preco, String unidade, Boolean inativo, Boolean servico, Boolean estoque, Boolean producao, int usuario) {
        String sql = "insert into produto (descricao, preco, unidade, inativo, servico, estoque, producao, dataModificacao, usuario) values (?,?,?,?,?,?,?,?,?)";
        try {

            //Remove o ponto do preco
            preco = preco.replace(".", ""); //Troca o ponto por nada
            //Troca a virgula por ponto
            preco = preco.replace(",", ".");

            pst = conexao.prepareStatement(sql);
            pst.setString(1, descricao);
            pst.setString(2, preco);
            pst.setString(3, unidade);
            pst.setBoolean(4, inativo);
            pst.setBoolean(5, servico);
            pst.setBoolean(6, estoque);
            pst.setBoolean(7, producao);
            pst.setDate(8, new Date(System.currentTimeMillis()));
            pst.setInt(9, usuario);
            pst.executeUpdate();
            return "CREATE";
        } catch (Exception e) {
            Message message = new Message("Erro!", "Erro ao cadastrar produto!" + e, "error");
            return "ERROR";
        }
    }

    public String editar(String id, String descricao, String preco, String unidade, Boolean inativo, Boolean servico, Boolean estoque, Boolean producao, int usuario) {
        String sql = "update produto set descricao=?, preco=?, unidade=?, inativo=?, servico=?, estoque=?, producao=?, dataModificacao=?, usuario=? where id=?";
        try {
            //Remove o ponto do preco
            preco = preco.replace(".", ""); //Troca o ponto por nada
            //Troca a virgula por ponto
            preco = preco.replace(",", ".");

            pst = conexao.prepareStatement(sql);
            pst.setString(1, descricao);
            pst.setString(2, preco);
            pst.setString(3, unidade);
            pst.setBoolean(4, inativo);
            pst.setBoolean(5, servico);
            pst.setBoolean(6, estoque);
            pst.setBoolean(7, producao);
            pst.setDate(8, new Date(System.currentTimeMillis()));
            pst.setInt(9, usuario);
            pst.setString(10, id);

            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
            return "ERROR";
        }
        return "SUCCESS";
    }

    public String excluir(String id) {
        String sql = "delete from produto where id=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.executeUpdate();
            return "DELETE";
        } catch (Exception e) {
            MessageView messageView = new MessageView("Erro!", "Produto já possui movimentação, não é possível excluir, mas pode ser inativado a qualquer momento!", "error");
            return "ERROR";
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
        String sql = "select id as ID, descricao as PRODUTO, unidade as UNIDADE, preco as PREÇO from produto where descricao like ?";;
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
                //Trocando o ponto por virgula
                String preco = rs.getString(3);
                preco = preco.replace(".", ",");
                produto.setPreco(preco);

                //produto.setPreco(rs.getDouble(3));
                produto.setUnidade(rs.getString(4));
                produto.setInativo(rs.getBoolean(5));
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
                fecharConexao();
                return rs.getInt(1);
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
}
