package com.leonardovechieti.dev.project.config;
import com.leonardovechieti.dev.project.model.Empresa;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigParametros {

	public static Properties getProp() throws IOException {
		Properties props = new Properties();
		FileInputStream file = new FileInputStream(
				"C:\\Dev\\parametros.properties");
		props.load(file);
		return props;
	}
     
    public Empresa getEmpresa() throws IOException{
        Properties prop = getProp();
        return new Empresa(
                prop.getProperty("prop.empresa.nome"),
                prop.getProperty("prop.empresa.razao"),
                prop.getProperty("prop.empresa.cnpj")
        );
    }

    public boolean setEmpresa(Empresa empresa) throws IOException{
        Properties properties = new Properties();
        properties.setProperty("prop.empresa.nome", empresa.getNome());
        properties.setProperty("prop.empresa.razao", empresa.getRazao());
        properties.setProperty("prop.empresa.cnpj", empresa.getCnpj());
	    try {
		    FileOutputStream fos = new FileOutputStream("C:\\Dev\\parametros.properties");
		    properties.store(fos, "C:\\Dev\\parametros.properties");
		    fos.close();
            return true;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
}