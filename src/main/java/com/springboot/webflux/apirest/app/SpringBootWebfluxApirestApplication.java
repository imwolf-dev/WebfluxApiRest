package com.springboot.webflux.apirest.app;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.springboot.webflux.apirest.app.models.documents.Categoria;
import com.springboot.webflux.apirest.app.models.documents.Producto;
import com.springboot.webflux.apirest.app.services.ProductoService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@SpringBootApplication
@Slf4j
public class SpringBootWebfluxApirestApplication implements CommandLineRunner{

	@Autowired
	private ProductoService productoService;


	@Autowired
	private ReactiveMongoTemplate reactiveMongoTemplate;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxApirestApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		
		// para eliminar la coleccion de productos y crearla de nuevo, podemos usar el ReactiveMongoTemplate
		reactiveMongoTemplate.dropCollection("productos").subscribe();
		reactiveMongoTemplate.dropCollection("categorias").subscribe();

		Categoria electronica = new Categoria("Electronica");
		Categoria deporte = new Categoria("Deporte");
		Categoria computacion = new Categoria("Computacion");
		Categoria muebles = new Categoria("Muebles");

		Flux.just(electronica, deporte, computacion, muebles)
			.flatMap(categoria -> productoService.saveCategoria(categoria))
			.doOnNext(c -> {
				log.info("categoria creada: " + c.getNombre() + " - con id: " + c.getId());
			})
			.thenMany( // thenMany 
				Flux.just(
				  new Producto("TV Panasonic Pantalla LCD", 456.89, electronica),
				  new Producto("Sony Camara HD Digital", 177.89, electronica),
				  new Producto("Apple iPod", 46.89, electronica),
				  new Producto("Sony Notebook", 846.89, electronica),
				  new Producto("Hewlett Packard Multifuncional", 200.89, computacion),
				  new Producto("Bianchi Bicicleta", 70.89, deporte),
				  new Producto("HP Notebook Omen 17", 2500.89, computacion),
				  new Producto("Mica Comoda 5 Cajones", 150.89, muebles),
				  new Producto("TV Sony Bravia OLED 4K Ultra HD", 2255.89, electronica)
				)	
				//.flatMap(productoDao::save)
				.flatMap(producto -> {
					producto.setCreateAt(new Date());
					return productoService.save(producto);
				})
				.doOnNext(p -> log.info("Insert: {} {}", p.getId(), p.getNombre()))
			)		
			//.subscribe(producto -> log.info("Insert: " + producto.getId() + " " + producto.getNombre()));
			.subscribe();
	}

}
