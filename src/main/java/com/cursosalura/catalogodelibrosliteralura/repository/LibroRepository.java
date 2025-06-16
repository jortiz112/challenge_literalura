package com.cursosalura.catalogodelibrosliteralura.repository;

import com.cursosalura.catalogodelibrosliteralura.modelos.Idioma;
import com.cursosalura.catalogodelibrosliteralura.modelos.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    //Para validar si existe el libro en la base de datos
    Optional<Libro> findByTitulo(String titulo);

    //Para obtener la cantidad de libros por idioma
    @Query("SELECT l FROM Libro l WHERE l.idioma = :idioma")
    List<Libro> buscarLibrosPorIdioma(Idioma idioma);
}
