package com.leonardovechieti.dev.project.util;

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
}

