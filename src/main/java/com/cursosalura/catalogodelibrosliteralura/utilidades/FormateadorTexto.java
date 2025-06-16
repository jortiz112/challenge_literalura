package com.cursosalura.catalogodelibrosliteralura.utilidades;

public class FormateadorTexto {
    public static void imprimirTextoFormateado(String texto, int anchoLinea) {
        int longitud = texto.length();
        int inicio = 0;
        while (inicio < longitud) {
            int fin = Math.min(inicio + anchoLinea, longitud);
            System.out.println(texto.substring(inicio, fin));
            inicio = fin;
        }
    }
}
