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
        conexao = ModuloConexao.conector();
        String slq = "select * from usuario where login = ? and senha = ? and ativo=1";
        try {
            pst  = conexao.prepareStatement(slq);
            pst.setString(1, login);
            pst.setString(2, senha);

            rs = pst.executeQuery();

            if (rs.next()) {

                System.out.println("Usuario logado: " + rs.getString("nome"));
                //conexao.close(); //termina a conexao com o banco
                return new Usuario(rs.getInt("id"), rs.getString("nome"), rs.getString("login"), rs.getString("senha"), rs.getBoolean("ativo"), rs.getString("perfil"));

            }else {
                JOptionPane.showMessageDialog(null, "Usuario ou senha inválidos!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        return null;
    }
}
