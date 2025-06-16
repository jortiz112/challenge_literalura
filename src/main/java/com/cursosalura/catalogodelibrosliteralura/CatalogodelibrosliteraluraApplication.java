package com.cursosalura.catalogodelibrosliteralura;

import com.cursosalura.catalogodelibrosliteralura.principal.Principal;
import com.cursosalura.catalogodelibrosliteralura.repository.AutorRepository;
import com.cursosalura.catalogodelibrosliteralura.repository.LibroDetalleRepository;
import com.cursosalura.catalogodelibrosliteralura.repository.LibroRepository;
import com.cursosalura.catalogodelibrosliteralura.servicios.GeminiTranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CatalogodelibrosliteraluraApplication implements CommandLineRunner {

	//Esto le va a indicar a Spring que le haga una inyección de dependencias
	@Autowired
	private LibroRepository libroRepository;
	@Autowired
	private AutorRepository autorRepository;
	@Autowired
	private LibroDetalleRepository libroDetalleRepository;
	@Autowired
	private GeminiTranslationService translator;

	public static void main(String[] args) {
		SpringApplication.run(CatalogodelibrosliteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(libroRepository, autorRepository, libroDetalleRepository, translator);
		principal.muestraElMenu();
	}

}
