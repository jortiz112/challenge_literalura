package com.cursosalura.catalogodelibrosliteralura.repository;

import com.cursosalura.catalogodelibrosliteralura.modelos.Libro;
import com.cursosalura.catalogodelibrosliteralura.modelos.LibroDetalle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibroDetalleRepository extends JpaRepository<LibroDetalle, Long> {
    //Para validar si existe el libro en la base de datos
    Optional<LibroDetalle> findByTitulo(String titulo);
}
