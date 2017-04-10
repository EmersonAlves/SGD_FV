package com.sgdfv.emerson.sgd_fv.util;

import java.text.DecimalFormat;

public class MascaraUtil {

    public static double recuperarValorCampoMoeda(String valorTexto) {
        valorTexto = valorTexto.replaceAll("\\.", ",");
        int index = valorTexto.lastIndexOf(",");
        String valorTextoInteiro = valorTexto.substring(0, index);
        valorTextoInteiro = valorTextoInteiro.replaceAll(",", "");
        String valorTextoDecimal = valorTexto.substring(index);
        valorTextoDecimal = valorTextoDecimal.replaceAll(",", ".");

        valorTexto = valorTextoInteiro + valorTextoDecimal;
        return Double.parseDouble(valorTexto);
    }


    public static String setValorCampoMoeda(double valor) {

        DecimalFormat df = new DecimalFormat("0.00");
        String valorTexto = df.format(valor);

        valorTexto = valorTexto.replaceAll("\\.", ",");
        int index = valorTexto.lastIndexOf(",");
        String valorTextoInteiro = valorTexto.substring(0, index);
        valorTextoInteiro = valorTextoInteiro.replaceAll(",", ".");
        String valorTextoDecimal = valorTexto.substring(index);

        valorTexto = valorTextoInteiro + valorTextoDecimal;

        return valorTexto;

    }

    public static String getValorCNPJSemMascara(String valor) {
        String x = valor.replaceAll("/", "");
        x = x.replaceAll("\\.", "");
        x = x.replaceAll("-", "");
        return x;
    }

    public static String getValorCPFSemMascara(String valor) {
        String x = valor.replaceAll("\\.", "");
        x = x.replaceAll("-", "");
        return x;
    }

    public static String getValorTelefoneSemMascara(String valor) {
        String x = valor.replaceAll("\\(", "");
        x = x.replaceAll("\\)", "");
        x = x.replaceAll("-", "");
        x = x.replaceAll(" ", "");
        return x;
    }

    public static String getValorCEPSemMascara(String valor) {
        String x = valor.replaceAll("-", "");
        return x;
    }

    public static void main(String args[]) {
        String x = "(81) 3452-2481";
        System.out.println(getValorTelefoneSemMascara(x));
    }
}
