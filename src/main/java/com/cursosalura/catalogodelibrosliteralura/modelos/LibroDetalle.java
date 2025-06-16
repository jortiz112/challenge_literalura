
package com.cursosalura.catalogodelibrosliteralura.modelos;


import com.cursosalura.catalogodelibrosliteralura.servicios.GeminiTranslationService;
import com.cursosalura.catalogodelibrosliteralura.utilidades.FormateadorTexto;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "librodetalles")
public class LibroDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private String nombreAutor;
    //Permite que Postgresql guarde completo la sinopsis, aunque sea largo
    @Column(columnDefinition = "TEXT")
    private String sinopsis;

    public LibroDetalle(){
    }

    public LibroDetalle(DatosLibrosDetalle datosLibrosDetalle, GeminiTranslationService translator) {
        this.titulo = datosLibrosDetalle.titulo();

        this.nombreAutor = datosLibrosDetalle.autor().stream()
                .findFirst()
                .map(DatosAutor::nombre)
                .orElse("Desconocido");

        this.sinopsis = translator.traducir(String.join(" ", datosLibrosDetalle.sinopsis()));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", nombreAutor='" + nombreAutor + '\'' +
                ", sinopsis=" + sinopsis;
    }

    public void imprimirLibroTraducido() {
        System.out.println("""
                \n\033[96m█▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬█
                \033[93m    📘 Libro Traducido:""" + " " + titulo.toUpperCase() + """
                \n\033[96m█▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬█
                """);
        System.out.println("\033[0mTitulo: " + titulo);
        System.out.println("Autor: " + nombreAutor);
        System.out.println("Sinopsis traducida:");
        //distribuye la impresión en varias líneas para ver la traducción
        FormateadorTexto.imprimirTextoFormateado(sinopsis, 80);
    }
}

