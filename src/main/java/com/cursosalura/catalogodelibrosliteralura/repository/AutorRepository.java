package com.cursosalura.catalogodelibrosliteralura.repository;

import com.cursosalura.catalogodelibrosliteralura.modelos.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    //para validar la existencia del nombre del autor en la base de datos
    Optional<Autor> findByNombre(String nombre);
    //para recuperar todos los autores que estaban vivos en el año que el usuario ingresa
    //con derived queries
    List<Autor> findByFechaDeNacimientoLessThanEqualAndFechaDeFallecimientoGreaterThanEqual(Integer anioNacimiento, Integer anioFallecimiento);
    //Para buscar el autor por su nombre
    Optional<Autor> findByNombreContainsIgnoreCase(String nombreAutor);

}
