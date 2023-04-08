package com.leonardovechieti.dev.project.dao;

import com.leonardovechieti.dev.project.config.ConfigDao;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ModuloConexao {
    
    
    public static Connection conector() {
        
         ConfigDao config = new ConfigDao();
        try {
            config.lerarquivo();
        } catch (IOException ex) {
            Logger.getLogger(ModuloConexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Connection conexao = null;
        String driver = "com.mysql.cj.jdbc.Driver";
        String url= config.getUrl();
        System.out.println("Nome = "+url);
        String user=config.getUser();
        String password=config.getPassword();

        //
         //String url= "jdbc:mysql://localhost:3306/dev";
         //String user="root";
         //String password=  "1234";
            try {
                Class.forName(driver);
                conexao = DriverManager.getConnection(url, user, password);
                return conexao;
            } catch (Exception e) {
                System.out.println(e);
                return null ;
            }
    }
    
    
    
}
