package com.cursosalura.catalogodelibrosliteralura.servicios;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
