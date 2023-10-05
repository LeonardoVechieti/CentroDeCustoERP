package com.leonardovechieti.dev.project.util;

import java.text.NumberFormat;
import java.util.Locale;

public class Func {

    public Func() {}

    public static String formataData(String data) {
        String dataFormatada = data.substring(8, 10) + "/" + data.substring(5, 7) + "/" + data.substring(0, 4);
        return dataFormatada;
    }

    public static String formatarString(String texto) {
        String textoFormatado = texto.toLowerCase();
        textoFormatado = textoFormatado.replace("  ", " ");
        textoFormatado = textoFormatado.replace("ã", "a");
        textoFormatado = textoFormatado.replace("á", "a");
        textoFormatado = textoFormatado.replace("à", "a");
        textoFormatado = textoFormatado.replace("â", "a");
        textoFormatado = textoFormatado.replace("é", "e");
        textoFormatado = textoFormatado.replace("ê", "e");
        textoFormatado = textoFormatado.replace("í", "i");
        textoFormatado = textoFormatado.replace("ó", "o");
        textoFormatado = textoFormatado.replace("õ", "o");
        textoFormatado = textoFormatado.replace("ô", "o");
        textoFormatado = textoFormatado.replace("ú", "u");
        textoFormatado = textoFormatado.replace("ç", "c");
        return textoFormatado;
    }

    public static String removeEspaco(String texto) {
        String textoFormatado = texto.replace(" ", "_");
        textoFormatado = textoFormatado.replace("  ", "_");
        textoFormatado = textoFormatado.replace("   ", "_");
        return textoFormatado;
    }

    public static Number arredondar(Number valor) {
        return Math.round(valor.doubleValue() * 100.0) / 100.0;
    }


    public static String trocaVirgulaPorPonto(String valor) {
        String valorFormatado = valor;
        valorFormatado = valorFormatado.replace(",", ".");
        return valorFormatado;
    }

    public static String formataPrecoBanco(String preco) {
        if (preco==null || preco.equals("")) {
            return "0.00";
        } else {
            //Remove o ponto do preco
            preco = preco.replace(".", ""); //Troca o ponto por nada
            //Troca a virgula por ponto
            preco = preco.replace(",", ".");
            return preco;
        }
    }

    public static String formataPrecoPadrao(String preco) {
        if (preco==null || preco.equals("")) {
            return "0,00";
        } else {
            double valor = Double.parseDouble(preco);
            // Criar o formatador para o valor monetário
            NumberFormat formatador = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            // Formatar o valor como uma string
            String valorFormatado = formatador.format(valor);
            //Remover o R$
            valorFormatado = valorFormatado.replace("R$ ", "");
            // Exibir o valor formatado
            //System.out.println(valorFormatado);
            return valorFormatado;
        }
    }

    //Recebe uma string de data e formata para padrao de exibicao do sistema
    public static String formataDataPadrao(String data) {
        String dataFormatada = data.substring(8, 10) + "/" + data.substring(5, 7) + "/" + data.substring(0, 4);
        return dataFormatada;
    }

    //Recebe uma string de data e formata para padrao banco de dados
    public static String formataDataBanco(String data) {
        String dataFormatada = data.substring(6, 10) + "-" + data.substring(3, 5) + "-" + data.substring(0, 2);
        return dataFormatada;
    }

    //Valida se a string e uma data se ele
    public static boolean validaData(String data) {
        if (data.length() != 10) {
            return false;
        }
        if (!data.substring(2, 3).equals("/") || !data.substring(5, 6).equals("/")) {
            return false;
        }
        System.out.println("Data: " + data);
        if (data.length() != 10) {
            return false;
        }
        if (!data.substring(2, 3).equals("/") || !data.substring(5, 6).equals("/")) {
            return false;
        }
        int dia = Integer.parseInt(data.substring(0, 2));
        int mes = Integer.parseInt(data.substring(3, 5));
        int ano = Integer.parseInt(data.substring(6, 10));
        if (dia < 1 || dia > 31) {
            return false;
        }
        if (mes < 1 || mes > 12) {
            return false;
        }
        if (ano < 1900 || ano > 2100) {
            return false;
        }
        return true;
    }
}

