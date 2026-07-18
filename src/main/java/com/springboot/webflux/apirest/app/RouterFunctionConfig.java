package com.springboot.webflux.apirest.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.springboot.webflux.apirest.app.handler.ProductoHandler;
import com.springboot.webflux.apirest.app.models.documents.Producto;
import com.springboot.webflux.apirest.app.services.ProductoService;

@Configuration
public class RouterFunctionConfig {

    @Autowired
    private ProductoService service;

    @Bean
    public RouterFunction<ServerResponse> routes(ProductoHandler handler){

        // para definir una sola ruta
        //return RouterFunctions.route(RequestPredicates.GET("/api/v2/productos"), request -> {

        /* 
        // para definir MAS de una ruta usar OR
        return RouterFunctions
            .route(RequestPredicates.GET("/api/v2/productos")
            .or(RequestPredicates.GET("/api/v3/productos")), request -> {
                return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll(), Producto.class);
            });
        */

        // usando HANDLER
        // para definir MAS de una ruta usar OR
        return RouterFunctions
            .route(RequestPredicates.GET("/api/v4/productos")
            .or(RequestPredicates.GET("/api/v5/productos")), 
                request -> {
                    return handler.listar(request);
                }
            )
            .andRoute(
                RequestPredicates.GET("/api/v6/productos"),
                handler::listar // forma funcional
            )
            .andRoute(
                RequestPredicates.GET("/api/v2/productos/{id}"), handler::ver
            )
            ;
    }
}
