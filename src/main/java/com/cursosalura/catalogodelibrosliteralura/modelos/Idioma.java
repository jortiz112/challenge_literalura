package com.cursosalura.catalogodelibrosliteralura.modelos;

public enum Idioma {
    es("Español"),
    en("Inglés"),
    fr("Francés"),
    pt("Portugués");

    private String descripcionIdioma;

    Idioma(String descripcionIdioma) {
        this.descripcionIdioma = descripcionIdioma;
    }

    public static Idioma fromString(String text) {
        for (Idioma idioma : Idioma.values()) {
            if (idioma.name().equalsIgnoreCase(text)) {
                return idioma;
            }
        }
        throw new IllegalArgumentException("Ningún idioma encontrada: " + text);
    }

    public String getDescripcionIdioma() {
        return descripcionIdioma;
    }
}
