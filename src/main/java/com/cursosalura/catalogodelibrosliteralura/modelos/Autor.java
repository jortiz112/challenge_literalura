package com.cursosalura.catalogodelibrosliteralura.modelos;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Integer fechaDeNacimiento;
    private Integer fechaDeFallecimiento;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros;

    public Autor(){
    }

    public Autor(DatosAutor datosAutor) {
        if (datosAutor != null) {
            this.nombre = datosAutor.nombre();
            this.fechaDeNacimiento = datosAutor.fechaDeNacimiento();
            this.fechaDeFallecimiento = datosAutor.fechaDeFallecimiento();
        }else {
            this.nombre = "Desconocido";
            this.fechaDeNacimiento = null;
            this.fechaDeFallecimiento = null;
        }
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Integer fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public Integer getFechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }

    public void setFechaDeFallecimiento(Integer fechaDeFallecimiento) {
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }

    @Override
    public String toString() {
        return "Nombre='" + nombre + '\'' +
                ", FechaDeNacimiento=" + fechaDeNacimiento +
                ", FechaDeFallecimiento=" + fechaDeFallecimiento +
                ", Libros=" + libros;
    }

    public void imprimirAutor() {
        System.out.println("""
                \n\033[96m█▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬█
                \033[93m    👨 Autor:""" + " " + nombre.toUpperCase() + """
                \n\033[96m█▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬█
                """);
        System.out.println("\033[0m  Nombre: " + nombre);
        System.out.println("  FechaDeNacimiento: " + fechaDeNacimiento);
        System.out.println("  FechaDeFallecimiento: " + fechaDeFallecimiento);
        // Imprimir los títulos de los libros
        System.out.println("  Libros:");
        libros.forEach(libro -> System.out.println("    📘 " + libro.getTitulo()));
        System.out.println();
    }
}
