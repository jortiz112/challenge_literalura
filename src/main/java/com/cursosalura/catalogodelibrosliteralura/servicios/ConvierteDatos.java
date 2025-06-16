package com.cursosalura.catalogodelibrosliteralura.servicios;

import com.cursosalura.catalogodelibrosliteralura.excepcion.ExcepcionConversion;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvierteDatos implements IConvierteDatos {
    //clase ObjectMapper de Jackson, que es el núcleo para
    //transformar los datos JSON a objetos Java
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T obtenerDatos(String json, Class<T> clase) {
        try {
            return objectMapper.readValue(json,clase);
        } catch (JsonProcessingException e) {
            throw new ExcepcionConversion("Error al procesar la respuesta del servicio.");
        }
    }
}