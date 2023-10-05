package com.leonardovechieti.dev.project.repository;

import com.leonardovechieti.dev.project.dao.ModuloConexao;
import com.leonardovechieti.dev.project.model.CentroDeCusto;
import com.leonardovechieti.dev.project.model.Operacao;

import java.sql.*;

public class OperacaoRepository {
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public OperacaoRepository() {
        conexao = ModuloConexao.conector();
    }

    public String salvar(Operacao operacao) {
        String sql = "insert into operacao (descricao, operacao, receita, inativo) values (?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, operacao.getDescricao());
            pst.setString(2, operacao.getOperacao().toString());
            pst.setString(3, operacao.getReceita().toString());
            pst.setBoolean(4, operacao.getInativo());
            pst.executeUpdate();

            return "SUCCESS";
        } catch (Exception e) {
            System.out.println(e);
            return "ERROR";
        }
    }

    public String editar(Operacao operacao) {
        String sql = "update operacao set descricao=?, operacao=?, receita=?, inativo=? where id=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, operacao.getDescricao());
            pst.setString(2, operacao.getOperacao().toString());
            pst.setString(3, operacao.getReceita().toString());
            pst.setBoolean(4, operacao.getInativo());
            pst.setInt(5, operacao.getId());
            pst.executeUpdate();
            return "SUCCESS";
        } catch (Exception e) {
            System.out.println(e);
            return "ERROR";
        }
    }

    public String excluir(String id) {
        String sql = "delete from operacao where id=?";
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
        String sql = "select id as ID, descricao as DESCRICAO, operacao as OPERACAO, receita as RECEITA from operacao";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
        } catch (Exception e) {
            System.out.println(e);
        }
        return rs;
    }

    public Wrapper pesquisar(String descricao) {
        String sql = "select id as ID, descricao as DESCRICAO, operacao as OPERACAO from operacao where descricao like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, "%" + descricao + "%");
            rs = pst.executeQuery();
        } catch (Exception e) {
            System.out.println(e);
        }
        return rs;
    }

    public Operacao buscaId(String id) {
        String sql = "select * from operacao where id = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(id));
            rs = pst.executeQuery();
            if (rs.next()) {
                return new Operacao(
                        rs.getInt("id"),
                        rs.getString("descricao"),
                        rs.getString("operacao"),
                        rs.getString("receita"),
                        rs.getBoolean("inativo"));
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
        String sql = "select max(id) from operacao";
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
    //Retorna todos os nome do centro de custo em uma string
    public String todasDescricao() {
        String sql = "select descricao from operacao where inativo = false";
        //String sql = "select descricao from operacao";
        String descricao = "";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                descricao += rs.getString("descricao") + ",";
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return descricao;
    }

    //Retorna o id através da descrição
    public int buscaIdDescricao(String descricao) {
        String sql = "select id from operacao where descricao = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, descricao);
            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                return 0;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }
    //Retorna a opercao através da descrição
    public Operacao buscaOperacaoDescricao(String descricao) {
        String sql = "select * from operacao where descricao = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, descricao);
            rs = pst.executeQuery();
            if (rs.next()) {
                return new Operacao(
                        rs.getInt("id"),
                        rs.getString("descricao"),
                        rs.getString("operacao"),
                        rs.getString("receita"),
                        rs.getBoolean("inativo"));
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    //Retorna o tipo atraves do id
    public String buscaTipoId(String id) {
        String sql = "select operacao from operacao where id = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(id));
            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("operacao");
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    //Retorna a receita atraves do id
    public String buscaReceitaId(String id) {
        String sql = "select receita from operacao where id = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(id));
            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("receita");
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public void fecharConexao() throws SQLException {
        conexao.close();
        System.out.println("Conexao fechada!");
    }
}
