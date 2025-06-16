package com.cursosalura.catalogodelibrosliteralura.modelos;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

//Para ignorar el resto de datos o campos que no se está utilizando del API aquí
@JsonIgnoreProperties(ignoreUnknown = true)
public record Libros(
        @JsonAlias("results") List<DatosLibros> resultadoLibros
) {
}
