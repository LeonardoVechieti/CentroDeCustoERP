package com.leonardovechieti.dev.project.repository;

import com.leonardovechieti.dev.project.dao.ModuloConexao;
import com.leonardovechieti.dev.project.model.Usuario;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioRepository {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public UsuarioRepository() {
        conexao = ModuloConexao.conector();
    }

    public Usuario login(String login, String senha) {
        String slq = "select * from usuario where login = ? and senha = ? and inativo=0";
        try {
            pst  = conexao.prepareStatement(slq);
            pst.setString(1, login);
            pst.setString(2, senha);

            rs = pst.executeQuery();

            if (rs.next()) {
                System.out.println("Usuario logado: " + rs.getString("nome"));
                return new Usuario(rs.getInt("id"), rs.getString("nome"), rs.getString("login"), rs.getString("senha"), rs.getBoolean("inativo"), rs.getString("perfil"));

            }else {
                return null;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return null;
    }

    public Usuario buscaId(int usuario) {
        String slq = "select * from usuario where id = ?";
        try {
            pst  = conexao.prepareStatement(slq);
            pst.setInt(1, usuario);

            rs = pst.executeQuery();

            if (rs.next()) {
                return new
                        Usuario(rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("login"),
                        rs.getString("senha"),
                        rs.getBoolean("inativo"),
                        rs.getString("perfil"));
            }else {
                return null;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return null;
    }

    public String cadastrar(String nome, String login, String senha, Boolean inativo, String perfil) {
        String sql = "insert into usuario (nome, login, senha, inativo, perfil) values (?, ?, ?, ?, ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, nome);
            pst.setString(2, login);
            pst.setString(3, senha);
            pst.setBoolean(4, inativo);
            pst.setString(5, perfil);
            pst.executeUpdate();
            return "CREATE";
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return "ERROR";
        }
    }

    public Integer ultimoId() {
        String sql = "select max(id) from usuario";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }

    public String alterar(int id, String nome, String login, String senha, Boolean inativo, String perfil) {
        String sql = "update usuario set nome = ?, login = ?, senha = ?, inativo = ?, perfil = ? where id = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, nome);
            pst.setString(2, login);
            pst.setString(3, senha);
            pst.setBoolean(4, inativo);
            pst.setString(5, perfil);
            pst.setInt(6, id);
            pst.executeUpdate();
            return "SUCCESS";
        } catch (Exception e) {
            return "ERROR";
        }
    }

    public String excluir(int id) {
        String sql = "delete from usuario where id = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
            return "SUCCESS";
        } catch (Exception e) {
            return "ERROR";
        }
    }

    public Usuario buscaUsuario(String id) {
        String slq = "select * from usuario where id = ?";
        try {
            pst  = conexao.prepareStatement(slq);
            pst.setString(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                return new
                        Usuario(rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("login"),
                        rs.getString("senha"),
                        rs.getBoolean("inativo"),
                        rs.getString("perfil"));
            }else {
                return null;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return null;
    }

    public ResultSet buscaUsuarios() {
        String slq = "select id as ID, nome as NOME, login as LOGIN from usuario";
        try {
            pst  = conexao.prepareStatement(slq);
            rs = pst.executeQuery();
            return rs;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return null;
    }

    public void fecharConexao() {
        try {
            conexao.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
}
