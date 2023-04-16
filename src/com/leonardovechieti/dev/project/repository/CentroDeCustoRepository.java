package com.leonardovechieti.dev.project.repository;

import com.leonardovechieti.dev.project.dao.ModuloConexao;
import com.leonardovechieti.dev.project.model.CentroDeCusto;

import java.sql.*;

public class CentroDeCustoRepository {
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public CentroDeCustoRepository() {
        conexao = ModuloConexao.conector();
    }

    public String salvar(CentroDeCusto centroDeCusto) {
        String sql = "insert into centrodecusto (nome, descricao, inativo) values (?, ?, ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, centroDeCusto.getNome());
            pst.setString(2, centroDeCusto.getDescricao());
            pst.setBoolean(3, centroDeCusto.getInativo());
            pst.executeUpdate();

            return "SUCCESS";
        } catch (Exception e) {
            System.out.println(e);
            return "ERROR";
        }
    }

    public String editar(CentroDeCusto centroDeCusto) {
        String sql = "update centrodecusto set nome=?, descricao=?, inativo=? where id=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, centroDeCusto.getNome());
            pst.setString(2, centroDeCusto.getDescricao());
            pst.setBoolean(3, centroDeCusto.getInativo());
            pst.setInt(4, centroDeCusto.getId());
            pst.executeUpdate();
            return "SUCCESS";
        } catch (Exception e) {
            System.out.println(e);
            return "ERROR";
        }
    }

    public String excluir(String id) {
        String sql = "delete from centrodecusto where id=?";
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
        String sql = "select * from centrodecusto";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
        } catch (Exception e) {
            System.out.println(e);
        }
        return rs;
    }

    public Wrapper pesquisar(String nome) {
        String sql = "select id as ID, nome as NOME from centrodecusto where nome like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, "%" + nome + "%");
            rs = pst.executeQuery();
        } catch (Exception e) {
            System.out.println(e);
        }
        return rs;
    }

    public CentroDeCusto buscaId(String id) {
        String sql = "select * from centrodecusto where id = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(id));
            rs = pst.executeQuery();
            if (rs.next()) {
                return new CentroDeCusto(rs.getInt("id"), rs.getString("nome"), rs.getString("descricao"), rs.getBoolean("inativo"));
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
        String sql = "select max(id) from centrodecusto";
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
}
