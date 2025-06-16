package com.cursosalura.catalogodelibrosliteralura.utilidades;

import com.cursosalura.catalogodelibrosliteralura.excepcion.ExcepcionConversion;

import java.util.Scanner;

public class ValidacionEntrada {


    public static int leerOpcion(Scanner teclado) {
        //En la entrada del usuario se elimina los espacios en blanco
        String entrada = teclado.nextLine().trim();

        if (entrada.matches("^\\d+$")) {
            return Integer.parseInt(entrada);
        } else {
            // opción inválida entra en default en menu
            return -1;
        }
    }

    public static int leerFecha(String fecha) {
        while (true) {
            if (fecha.matches("^[0-9]+(\\.[0-9]+)?$")) {
                return Integer.parseInt(fecha);
            } else {
                throw new ExcepcionConversion("❌ \033[31mFecha inválida. Debe ingresar un número válido (ej: 1700, 1892).");

            }
        }
    }
}
