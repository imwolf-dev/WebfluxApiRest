package com.springboot.webflux.apirest.app.models.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.springboot.webflux.apirest.app.models.documents.Categoria;

import reactor.core.publisher.Mono;

public interface CategoriaDao extends ReactiveMongoRepository<Categoria, String> {

    // metodo personalizado
    public Mono<Categoria> findByNombre(String nombre);
}
