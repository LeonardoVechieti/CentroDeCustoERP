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
        String slq = "select * from usuario where login = ? and senha = ? and ativo=1";
        try {
            pst  = conexao.prepareStatement(slq);
            pst.setString(1, login);
            pst.setString(2, senha);

            rs = pst.executeQuery();

            if (rs.next()) {
                System.out.println("Usuario logado: " + rs.getString("nome"));
                return new Usuario(rs.getInt("id"), rs.getString("nome"), rs.getString("login"), rs.getString("senha"), rs.getBoolean("ativo"), rs.getString("perfil"));

            }else {
                return null;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return null;
    }
}
