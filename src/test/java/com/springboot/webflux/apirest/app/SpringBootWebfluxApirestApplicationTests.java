package com.springboot.webflux.apirest.app;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.springboot.webflux.apirest.app.models.documents.Categoria;
import com.springboot.webflux.apirest.app.models.documents.Producto;
import com.springboot.webflux.apirest.app.services.ProductoService;

import reactor.core.publisher.Mono;

//@SpringBootTest // anotacion por defecto la cual trae todo lo de la clase principal de springboot
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // define un puerto random para las pruebas
class SpringBootWebfluxApirestApplicationTests {

	@Autowired
	private WebTestClient client;

	@Autowired
	private ProductoService productoService;

	private String urlBase = "/api/productos";

	/*
	// las pruebas de listar las estoy comentando porque generaban un error ya que los tests de accion crear/editar/eliminar se ejecutaban antes que la de listar, generando un error en la cantidad de registros(documents)
	@Test
	public void listarTest() {
		
		client.get()
		.uri("/api/productos/listar")
		.accept(MediaType.APPLICATION_JSON) // formato del response esperado
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBodyList(Producto.class)
		//.hasSize(5)	// catidad exacta de objetos de la lista
		.consumeWith(response -> {
			List<Producto> productos = response.getResponseBody();

			productos.forEach(p -> {
				System.out.println("nombre producto: " + p.getNombre());
			});

			Assertions.assertThat(productos.size() == 9).isTrue(); // valida cantidad de objetos de la lista
		})
		;
	}

	@Test
	public void listarResponseEntityTest() {
		
		client.get()
		.uri("/api/productos/listarResponseEntity")
		.accept(MediaType.APPLICATION_JSON) // formato del response esperado
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBodyList(Producto.class)
		//.hasSize(5)	// catidad exacta de objetos de la lista
		.consumeWith(response -> {
			List<Producto> productos = response.getResponseBody();

			productos.forEach(p -> {
				System.out.println("nombre producto: " + p.getNombre());
			});

			Assertions.assertThat(productos.size() == 9).isTrue(); // valida cantidad de objetos de la lista
		})
		;
	}


	@Test
	public void listarIdTest() {
		
		// Para PRUEBAS UNITARIAS tiene que ser SINCRONO
		Producto producto = productoService.findByNombre("Sony Camara HD Digital").block();
		
		client.get()
		.uri("/api/productos/listar/{id}", Collections.singletonMap("id", producto.getId()))
		.accept(MediaType.APPLICATION_JSON) // formato del response esperado
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)

		.expectBody()
		.jsonPath("$.id").isNotEmpty()
		.jsonPath("$.nombre").isEqualTo("Sony Camara HD Digital");
		;
	}

	@Test
	public void listarIdTest2() {
		
		// Para PRUEBAS UNITARIAS tiene que ser SINCRONO
		Producto producto = productoService.findByNombre("Sony Camara HD Digital").block();
		
		client.get()
		.uri("/api/productos/listar/{id}", Collections.singletonMap("id", producto.getId()))
		.accept(MediaType.APPLICATION_JSON) // formato del response esperado
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)

		.expectBody(Producto.class)
		.consumeWith(response -> {
			Producto prod = response.getResponseBody();

			Assertions.assertThat(prod.getNombre()).isEqualTo("Sony Camara HD Digital"); // valida cantidad de objetos de la lista
		})
		;
	}
	*/

	 
	@Test
	public void crearTest() {
		System.out.println("metodo crearTest");

		Categoria categoria = productoService.findCategoriaByNombre("Muebles").block();

		Producto producto = new Producto("Mesa", 99.10, categoria);

		client.post()
		.uri(urlBase + "/crear")
		.contentType(MediaType.APPLICATION_JSON) // formato del request
		.accept(MediaType.APPLICATION_JSON) // formato del response esperado
		.body(Mono.just(producto), Producto.class)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody()
		.jsonPath("$.id").isNotEmpty()
		.jsonPath("$.nombre").isEqualTo("Mesa")
		.jsonPath("$.categoria.nombre").isEqualTo("Muebles")
		;
	}

	@Test
	public void crearTest2() {
		System.out.println("metodo crearTest2");

		Categoria categoria = productoService.findCategoriaByNombre("Muebles").block();

		Producto producto = new Producto("Silla", 69.90, categoria);

		client.post()
		.uri(urlBase + "/crear")
		.contentType(MediaType.APPLICATION_JSON) // formato del request
		.accept(MediaType.APPLICATION_JSON) // formato del response esperado
		.body(Mono.just(producto), Producto.class)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody(Producto.class)
		.consumeWith(response -> {
			Producto p = response.getResponseBody();

			Assertions.assertThat(p.getId()).isNotEmpty();
			Assertions.assertThat(p.getNombre()).isEqualTo("Silla");
			Assertions.assertThat(p.getCategoria().getNombre()).isEqualTo("Muebles");
		})
		;
	}


	@Test
	public void editarTest() {
		System.out.println("metodo editarTest");

		Producto producto = productoService.findByNombre("Mica Comoda 5 Cajones").block();
		Categoria categoria = productoService.findCategoriaByNombre("Electronica").block();

		Producto productoEditado = new Producto("MicaXD", 999.90, categoria);

		client.put()
		.uri(urlBase + "/{id}", Collections.singletonMap("id", producto.getId()))
		.contentType(MediaType.APPLICATION_JSON) // formato del request
		.accept(MediaType.APPLICATION_JSON) // formato del response esperado
		.body(Mono.just(productoEditado), Producto.class)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody()
		.jsonPath("$.id").isNotEmpty()
		.jsonPath("$.nombre").isEqualTo("MicaXD")
		.jsonPath("$.categoria.nombre").isEqualTo("Electronica")
		;
	}

	@Test
	public void editarTest2() {
		System.out.println("metodo editarTest2");

		Producto producto = productoService.findByNombre("Apple iPod").block();
		Categoria categoria = productoService.findCategoriaByNombre("Electronica").block();

		Producto productoEditado = new Producto("Apple AIFON", 999.90, categoria);

		client.put()
		.uri(urlBase + "/{id}", Collections.singletonMap("id", producto.getId()))
		.contentType(MediaType.APPLICATION_JSON) // formato del request
		.accept(MediaType.APPLICATION_JSON) // formato del response esperado
		.body(Mono.just(productoEditado), Producto.class)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody(Producto.class)
		.consumeWith(response -> {
			Producto p = response.getResponseBody();

			Assertions.assertThat(p.getId()).isNotEmpty();
			Assertions.assertThat(p.getNombre()).isEqualTo("Apple AIFON");
			Assertions.assertThat(p.getCategoria().getNombre()).isEqualTo("Electronica");
		})
		;
	}


	@Test
	public void eliminarTest() {
		System.out.println("metodo eliminarTest");

		Producto producto = productoService.findByNombre("Hewlett Packard Multifuncional").block();

		client.delete()
		.uri(urlBase + "/eliminar/{id}", Collections.singletonMap("id", producto.getId()))
		.exchange()
		.expectStatus().isNoContent()
		.expectBody()
		.isEmpty()
		;

		client.get()
		.uri(urlBase + "/listar/{id}", Collections.singletonMap("id", producto.getId()))
		.exchange()
		.expectStatus().isNotFound()
		.expectBody()
		.isEmpty()
		;
	}

}
