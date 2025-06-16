package com.cursosalura.catalogodelibrosliteralura.servicios;

import com.cursosalura.catalogodelibrosliteralura.excepcion.ExcepcionConversion;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoAPI {
    public String obtenerDatos(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "Java HttpClient")
                    .build();

            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new ExcepcionConversion("Error del servidor. Código de estado HTTP: " + response.statusCode());
            }

            String json = response.body();
            if (json == null || json.isBlank()) {
                throw new ExcepcionConversion("La respuesta del servicio está vacía.");
            }
            return json;

        } catch (IOException | InterruptedException e) {
            throw new ExcepcionConversion("Error de conexión con el API: " + e.getMessage());
        }

    }
}


