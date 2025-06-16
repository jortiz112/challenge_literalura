package com.cursosalura.catalogodelibrosliteralura.principal;

import com.cursosalura.catalogodelibrosliteralura.excepcion.ExcepcionConversion;
import com.cursosalura.catalogodelibrosliteralura.modelos.*;
import com.cursosalura.catalogodelibrosliteralura.repository.AutorRepository;
import com.cursosalura.catalogodelibrosliteralura.repository.LibroDetalleRepository;
import com.cursosalura.catalogodelibrosliteralura.repository.LibroRepository;
import com.cursosalura.catalogodelibrosliteralura.servicios.ConsumoAPI;
import com.cursosalura.catalogodelibrosliteralura.servicios.ConvierteDatos;
import com.cursosalura.catalogodelibrosliteralura.servicios.GeminiTranslationService;
import com.cursosalura.catalogodelibrosliteralura.utilidades.FormateadorTexto;
import com.cursosalura.catalogodelibrosliteralura.utilidades.ValidacionEntrada;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);
    private LibroRepository libroRepositorio;
    private AutorRepository autorRepositorio;
    private List<Libro> libros;
    private List<Autor> autores;
    private String json;
    private LibroDetalleRepository libroDetalleRepositorio;
    private GeminiTranslationService translator;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository, LibroDetalleRepository libroDetalleRepository, GeminiTranslationService translator) {
        this.libroRepositorio = libroRepository;
        this.autorRepositorio = autorRepository;
        this.libroDetalleRepositorio = libroDetalleRepository;
        this.translator = translator;
    }

    public void muestraElMenu() {
        int opcion = -1;
        do {
            System.out.println("""
                    \n\033[96m█▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬█
                    ▬▬▬▬▬\033[32m=====\033[95mMENÚ PRINCIPAL DEL CATÁLOGO DE LIBROS\033[32m=====\033[96m▬▬▬▬▬
                    █▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬█
                        \033[32m1.- \033[93mBuscar libro por título
                        \033[32m2.- \033[93mListar libros registrados
                        \033[32m3.- \033[93mListar autores registrados
                        \033[32m4.- \033[93mListar autores vivos en un determinado año
                        \033[32m5.- \033[93mListar libros por idioma
                        \033[32m6.- \033[93mListar libros y Autores en un rango de fechas desde API
                        \033[32m7.- \033[93mTop 10 libros más descargados desde API
                        \033[32m8.- \033[93mBuscar autor por nombre desde BDD
                        \033[32m9.- \033[93mGenerando estadísticas desde API
                        \033[32m10.- \033[93mGemini traducción sinopsis libro
                    
                        \033[32m0.- \033[31mSalir
                    \033[96m█▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬█
                    █▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬█
                    """);
            System.out.println("\033[95mPor favor ingrese el número de la opción:");
            System.out.print("\033[91m►►► \033[0m");

            opcion = ValidacionEntrada.leerOpcion(teclado);

            try {
                switch (opcion) {
                    case 1 -> buscarLibroWeb();
                    case 2 -> mostrarLibrosRegistrados();
                    case 3 -> mostrarAutoresRegistrados();
                    case 4 -> mostrarAutoresVivosEnFechaUsuario();
                    case 5 -> mostrarLibrosPorIdioma();
                    case 6 -> obtenerLibrosYAutoresEnUnRangoDeFechas();
                    case 7 -> top10LibrosMasDescargados();
                    case 8 -> obtenerAutorPorNombre();
                    case 9 -> obtenerEstadisticas();
                    case 10 -> traduccionSinopsisLibro();
                    case 0 -> {
                        System.out.println("\033[32m☻☻ \033[34mSaliendo de la aplicación de catálogo de libros. ¡Hasta pronto, disfrute su día!\033[32m ☻☻");
                        //esto es necesario para cerrar los hilos abiertos (Reactor Netty) de Webclient
                        System.exit(0);
                    }
                    default -> System.out.println("⚠️ \033[34mOpción inválida. Intente nuevamente.\n");

                }
            } catch (ExcepcionConversion e) {
                System.out.println("\033[31m" + e.getMessage());
            } catch (Exception e) {
                System.out.println("\033[31m❌ Ocurrió un error inesperado: " + e.getMessage() + "\n");
            }

        } while (opcion != 0);

    }

    //Se busca un libro específico dado por el usuario consumiendo la API
    private DatosLibros obtenerDatosLibros() {
        System.out.println("\033[95mIngrese el nombre del libro que desea buscar:");
        System.out.print("\033[91m►►► ");
        var nombreLibro = teclado.nextLine();
        json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        Libros libros = conversor.obtenerDatos(json, Libros.class);

        if (libros.resultadoLibros().isEmpty()) {
            throw new ExcepcionConversion("⚠️ No se encontraron libros con ese nombre.\n");
        }
        DatosLibros libro = libros.resultadoLibros().get(0);
        return libro;

    }

    //Para imprimir la salida con formato especificado
    private void imprimirDatosLibros(DatosLibros libro) {
        //se formatea la salida
        System.out.println("""
                \n\033[96m█▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬█
                \033[95m    📘 Información del libro:""" + " " + libro.titulo().toUpperCase() + """
                \n\033[96m█▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬█
                """);
        System.out.println("\033[97m    Titulo: " + libro.titulo());
        //Autores
        if (!libro.autor().isEmpty()) {
            var autor = libro.autor().get(0);
            System.out.println("    Autor: " + autor.nombre());
        } else {
            System.out.println("Autor: Desconocido");
        }
        // Idiomas
        String idioma = libro.idiomas().isEmpty() ? "Desconocido" : libro.idiomas().get(0);
        System.out.println("    Idioma: " + idioma);

        System.out.println("    Número de descargas: " + libro.numeroDeDescargas() + "\n");
    }

    //Para validar si existe el autor en la base, caso contrario
    // guardamos al nuevo autor en la base de datos
    private Autor obtenerAutor(DatosLibros libros) {
        String nombreAutor = libros.autor().stream()
                .findFirst()
                .map(DatosAutor::nombre)
                .orElse("Desconocido");

        //busco el nombre autor en la base de datos
        Autor autor = autorRepositorio.findByNombre(nombreAutor)
                .orElseGet(() -> {
                    //obtengo los DatosAutor para el ingreso a la base
                    DatosAutor datosAutor = libros.autor().get(0);
                    Autor nuevoAutor = new Autor(datosAutor);
                    //grabo los datos del nuevo autor en la base
                    return autorRepositorio.save(nuevoAutor);
                });
        return autor;
    }

    //busca el libro ingresado por el usuario, valida si el mismo existe en la base de datos
    //antes de ingresarlo, valida de igual manera el autor en la base antes de ingresarlo
    private void buscarLibroWeb() {
        DatosLibros libros = obtenerDatosLibros();
        // Buscamos si el libro ya existe por título en la base de datos
        Optional<Libro> libroExistente = libroRepositorio.findByTitulo(libros.titulo());

        if (libroExistente.isPresent()) {
            System.out.println("⚠️ El libro ya existe en la base de datos.\n");
            return;
        }

        // Aquí aplicamos la lógica de verificar la existencia del autor en la base
        // o crear el autor si no existe en la misma
        Autor autor = obtenerAutor(libros);

        Libro libro = new Libro(libros);
        libro.setAutor(autor);

        //Grabo el libro en la base de datos
        libroRepositorio.save(libro);
        //mando a imprimir el libro en la consola
        imprimirDatosLibros(libros);
    }

    //Mostrar los libros que existen en la base de datos
    private void mostrarLibrosRegistrados() {
        libros = libroRepositorio.findAll();

        libros.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(l -> l.imprimirLibro());
    }

    //mostrar los autores que existen en la base de datos
    private void mostrarAutoresRegistrados() {
        autores = autorRepositorio.findAll();
        autores.stream()
                .sorted(Comparator.comparing(Autor::getNombre))
                .forEach(a -> a.imprimirAutor());
    }

    //Muestra los autores vivos en el intervalo de fechas comprendido
    // entre fecha de nacimiento y fallecimiento ingresando un año
    private void mostrarAutoresVivosEnFechaUsuario() {
        System.out.println("\033[95mIndique el año para listar los autores que estaban vivos durante ese periodo:");
        System.out.print("\033[91m►►► \033[0m");
        var fechaUsuario = teclado.nextInt();
        teclado.nextLine();
        List<Autor> autores = autorRepositorio.findByFechaDeNacimientoLessThanEqualAndFechaDeFallecimientoGreaterThanEqual(fechaUsuario, fechaUsuario);
        autores.stream()
                .sorted(Comparator.comparing(Autor::getNombre))
                .forEach(a -> a.imprimirAutor());

    }

    private void mostrarLibrosPorIdioma() {
        System.out.println("""
                \n\033[96m█▬▬\033[95mSeleccione una opción de idioma para buscar los libros\033[96m▬▬█
                █▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬█
                \t\033[32mes \033[31m►►►  \033[93mEspañol
                \t\033[32men \033[31m►►►  \033[93mInglés
                \t\033[32mfr \033[31m►►►  \033[93mFrancés
                \t\033[32mpt \033[31m►►►  \033[93mPortugués
                \033[96m█▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬█
                """);

        System.out.print("\033[91m►►► \033[0m");
        String opcion = teclado.nextLine().trim().toLowerCase();
        Idioma idiomaSeleccionado;
        try {
            idiomaSeleccionado = Idioma.fromString(opcion);
        } catch (IllegalArgumentException e) {
            throw new ExcepcionConversion ("\033[31m⚠️ Opción de idioma inválida.\n");
        }

        // Consultamos los libros usando JPQL
        List<Libro> librosPorIdioma = libroRepositorio.buscarLibrosPorIdioma(idiomaSeleccionado);

        if (librosPorIdioma.isEmpty()) {
            throw new ExcepcionConversion("\033[33m⚠️ No se encontraron libros en el idioma seleccionado.");
        }

        System.out.println("\n\033[92m📚 Libros encontrados en el idioma seleccionado:");

        // Uso de Streams
        librosPorIdioma.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(l -> l.imprimirLibro());

        System.out.println("\033[92mTotal de libros encontrados: " + librosPorIdioma.size() + "\n");
    }

    //Búsqueda de libros y Autores en un rango de fechas consumiendo la API
    private void obtenerLibrosYAutoresEnUnRangoDeFechas() {
        System.out.println("""
                \n\033[96m█▬▬▬\033[95mBusqueda de libros y autores en un rango de fechas:\033[96m▬▬▬█
                █▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬█
                """);
        System.out.println("\n\033[93mIngrese el año inicial de busqueda:");
        System.out.print("\033[91m►►► ");
        var fechaInicial = teclado.nextLine().trim();
        ValidacionEntrada.leerFecha(fechaInicial);
        System.out.println("\n\033[93mIngrese el año final de busqueda:");
        System.out.print("\033[91m►►► ");
        var fechaFinal = teclado.nextLine().trim();
        ValidacionEntrada.leerFecha(fechaFinal);
        json = consumoAPI.obtenerDatos(URL_BASE + "?author_year_start=" + fechaInicial + "&author_year_end=" + fechaFinal);
        var datosRangoFechas = conversor.obtenerDatos(json, Libros.class);
        datosRangoFechas.resultadoLibros().stream()
                .map(l -> "\n\033[93m" + l.titulo().toUpperCase() + "\033[0m - " + l.autor().stream()
                        .map(a -> a.nombre() + " " + a.fechaDeNacimiento() + " - " + a.fechaDeFallecimiento())
                        .collect(Collectors.joining(", "))+ " - " + l.idiomas())
                .forEach(System.out::println);
    }

    //Top 10 libros más descargados
    public void top10LibrosMasDescargados() {
        json = consumoAPI.obtenerDatos(URL_BASE);
        Libros libros = conversor.obtenerDatos(json, Libros.class);
        System.out.print("""
                \n\033[96m█▬▬▬\033[95m=Top 10 libros más descargados=\033[96m▬▬▬█
                █▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬█
                """);
        libros.resultadoLibros().stream()
                //reversed() de mayor a menor ordena
                .sorted(Comparator.comparing(DatosLibros::numeroDeDescargas).reversed())
                .limit(10)
                .map(l -> "\n\033[93m" + l.titulo().toUpperCase() + "\033[0m - " + l.numeroDeDescargas())
                .forEach(System.out::println);

    }

    //Buscar autor por nombre desde la base de datos
    private void obtenerAutorPorNombre() {
        System.out.println("\n\033[93mPor Favor ingrese el nombre del autor que desea encontrar:");
        System.out.print("\033[91m►►► \033[0m");
        String nombreAutor = teclado.nextLine();
        //busco el nombre del autor en la base de datos
        Optional<Autor> autor = autorRepositorio.findByNombreContainsIgnoreCase(nombreAutor);

        if (autor.isPresent()) {
            autor.get().imprimirAutor();
        } else {
            throw new ExcepcionConversion("\n⚠️ No se encontraron autores con ese nombre.");
        }
    }

    //Generando estadísticas
    private void obtenerEstadisticas() {
        json = consumoAPI.obtenerDatos(URL_BASE);
        Libros libros = conversor.obtenerDatos(json, Libros.class);
        DoubleSummaryStatistics estadisticas = libros.resultadoLibros().stream()
                .filter(d -> d.numeroDeDescargas() > 0)
                .collect(Collectors.summarizingDouble(DatosLibros::numeroDeDescargas));
        System.out.println("\n\033[35mEstadísticas\033[0m");
        System.out.println("\nCantidad media de descargas: \033[93m" + estadisticas.getAverage() + "\033[0m");
        System.out.println("Cantidad máxima de descargas: \033[93m" + estadisticas.getMax() + "\033[0m");
        System.out.println("Cantidad mínima de descargas: \033[93m" + estadisticas.getMin() + "\033[0m");
        System.out.println("Cantidad de descargas totales de los libros: \033[93m" + estadisticas.getSum() + "\033[0m");
        System.out.println("Cantidad de registros evaluados para calcular las estadísticas: \033[93m" + estadisticas.getCount() + "\033[0m");
        DoubleSummaryStatistics estAutores = libros.resultadoLibros().stream()
                //Sacamos todos los autores de todos los libros.
                .flatMap(l -> l.autor().stream())
                //nos aseguramos que el nombre no sea null
                .filter(a -> a.nombre() != null)
                //Se obtiene solo el nombre
                .map(DatosAutor::nombre)
                //Quito los repetidos
                .distinct()
                //aplicamos la estadística de conteo
                .collect(Collectors.summarizingDouble(nombre -> 1.0));

        System.out.println("Cantidad de autores distintos: \033[93m" + estAutores.getCount() + "\033[0m");
        DoubleSummaryStatistics statsAutoresFallecidos = libros.resultadoLibros().stream()
                .flatMap(l -> l.autor().stream())
                .filter(a -> a.nombre() != null)
                .collect(Collectors.toMap(
                        DatosAutor::nombre,    // clave: nombre
                        a -> a,                // valor: el propio autor
                        (a1, a2) -> a1         // si hay repetidos, dejamos el primero
                ))
                //obtenemos solo los valores (autores únicos)
                .values().stream()
                //filtramos solo los fallecidos
                .filter(a -> a.fechaDeFallecimiento() != null)
                //aplicamos la estadística de conteo
                .collect(Collectors.summarizingDouble(a -> 1.0));

        System.out.println("Cantidad de autores distintos fallecidos: \033[93m" + statsAutoresFallecidos.getCount() + "\033[0m");
    }

    private void traduccionSinopsisLibro() {
        System.out.println("\033[95mIngrese el nombre del libro que desea traducir su sipnosis:");
        System.out.print("\033[91m►►► ");
        var nombreLibroDetalle = teclado.nextLine();
        json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibroDetalle.replace(" ", "+"));
        LibrosDetalle librosDetalle = conversor.obtenerDatos(json, LibrosDetalle.class);

        if (librosDetalle.resultadoLibrosDestalle().isEmpty()) {
            throw new ExcepcionConversion("⚠️ No se encontraron libros con ese nombre.\n");
        }
        DatosLibrosDetalle libroDetalle = librosDetalle.resultadoLibrosDestalle().get(0);

        // Buscamos si el libro ya existe por título en la base de datos
        Optional<LibroDetalle> libroDetalleExistente = libroDetalleRepositorio.findByTitulo(libroDetalle.titulo());

        if (libroDetalleExistente.isPresent()) {
            System.out.println("⚠️ El libro ya existe en la base de datos.\n");
            return;
        }

        LibroDetalle libro = new LibroDetalle(libroDetalle, translator);

        //Grabo el libro en la base de datos
        libroDetalleRepositorio.save(libro);
        //mando a imprimir el libro en la consola
        libro.imprimirLibroTraducido();
    }

}
