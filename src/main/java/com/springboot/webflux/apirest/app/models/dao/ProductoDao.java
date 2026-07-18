package com.springboot.webflux.apirest.app.models.dao;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.springboot.webflux.apirest.app.models.documents.Producto;

import reactor.core.publisher.Mono;

public interface ProductoDao extends ReactiveMongoRepository<Producto, String> {

    // aqui podemos agregar metodos personalizados para consultas a la base de datos, por ejemplo:
    // Flux<Producto> findByNombre(String nombre);

    public Mono<Producto> findByNombre(String nombre);

    @Query("{'nombre': ?0}")
    public Mono<Producto> buscarNombre(String nombre);

}
