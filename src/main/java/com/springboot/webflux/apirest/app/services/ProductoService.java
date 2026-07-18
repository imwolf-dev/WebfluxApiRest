package com.springboot.webflux.apirest.app.services;

import com.springboot.webflux.apirest.app.models.documents.Categoria;
import com.springboot.webflux.apirest.app.models.documents.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {

    public Flux<Producto> findAll();

    public Mono<Producto> findById(String id);

    public Mono<Producto> save(Producto producto);

    public Mono<Void> delete(Producto producto);


    // metodos personalizados
    public Flux<Producto> findAllNombreUpperCase();

    public Flux<Producto> findAllNombreUpperCaseRepeat();


    /* --------------------------- */
    public Flux<Categoria> findAllCategoria();

    public Mono<Categoria> findCategoriaById(String id);

    public Mono<Categoria> saveCategoria(Categoria categoria);


    // metodo personalizado para el test buscarId
    public Mono<Producto> findByNombre(String nombre);
    // metodo personalizado para el test crear
    public Mono<Categoria> findCategoriaByNombre(String nombre);

}
