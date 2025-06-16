package com.cursosalura.catalogodelibrosliteralura.modelos;


import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    // Asegura que el autor se guarde automáticamente
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id")
    private Autor autor;
    @Enumerated(EnumType.STRING)
    private Idioma idioma;
    private Double numeroDeDescargas;

    public Libro(){
    }

    public Libro(DatosLibros datosLibros) {
        this.titulo = datosLibros.titulo();

        this.autor = datosLibros.autor().stream()
                .findFirst()
                .map(Autor::new)
                .orElse(null);

        this.idioma = Idioma.fromString(datosLibros.idiomas().get(0).trim());

        try{
            this.numeroDeDescargas = Double.valueOf(datosLibros.numeroDeDescargas());
        }catch (NumberFormatException e){
            this.numeroDeDescargas = 0.0;
        }
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

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    @Override
    public String toString() {
        return "Titulo='" + titulo + '\'' +
                ", Autor=" + autor +
                ", Idioma=" + idioma +
                ", Número de Descargas=" + numeroDeDescargas;
    }

    public void imprimirLibro() {
        System.out.println("""
                \n\033[96m█▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬█
                \033[93m    📘 Libro:""" + " " + titulo.toUpperCase() + """
                \n\033[96m█▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬█
                """);
        System.out.println("\033[0m  Titulo: " + titulo);
        System.out.println("  Autor: " + autor.getNombre());
        System.out.println("  Idioma: " + idioma);
        System.out.println("  Número de Descargas: " + numeroDeDescargas + "\n");
    }
}
