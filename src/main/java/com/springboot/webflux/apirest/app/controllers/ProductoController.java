package com.springboot.webflux.apirest.app.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.webflux.apirest.app.models.documents.Producto;
import com.springboot.webflux.apirest.app.services.ProductoService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;





@RestController
@RequestMapping("/api/productos")
@Slf4j
public class ProductoController {

    // Inyeccion mediante constructor de la clase
    private ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }


    @GetMapping("/listar")
    public Flux<Producto> listar() {
        log.info("metodo listar");

        return productoService.findAll();
    }
    

    // usando ResponseEntity
    @GetMapping("/listarResponseEntity")
    public Mono<ResponseEntity<Flux<Producto>>> listarResponseEntity() {
        log.info("metodo listarResponseEntity");

        return Mono.just(
            ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON) // para indicar que devolvera en formato json
            .body(productoService.findAll())
        );
    }


    @GetMapping("/listar/{id}")
    public Mono<ResponseEntity<Producto>> listarId(@PathVariable String id) {
        log.info("metodo listarId");

        return productoService.findById(id)
            .map(p -> ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON) // para indicar que devolvera en formato json
                .body(p)
            )
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @PostMapping("/crear")
    public Mono<ResponseEntity<Producto>> crear(@RequestBody Producto producto) {
        log.info("metodo crear");
        
        if(producto.getCreateAt() == null){
            producto.setCreateAt(new Date());
        }
        
        return productoService.save(producto)
            .map(p -> ResponseEntity.created(URI.create("/api/productos/listar/" + p.getId()))
                .contentType(MediaType.APPLICATION_JSON) // para indicar que devolvera en formato json
                .body(p)        
        );
    }


    @PutMapping("/{id}")
    public Mono<ResponseEntity<Producto>> editar(@RequestBody Producto producto, @PathVariable String id) {
        log.info("metodo editar");
        
        return productoService.findById(id)
            .flatMap(p -> {
                p.setNombre(producto.getNombre());
                p.setPrecio(producto.getPrecio());
                p.setCategoria(producto.getCategoria());
                return productoService.save(p);
            })
            .map(p -> ResponseEntity.created(URI.create("/api/productos/listar/" + p.getId()))
                .contentType(MediaType.APPLICATION_JSON) // para indicar que devolvera en formato json
                .body(p))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    

    // 1era forma de eliminar
    @DeleteMapping("eliminar/{id}")
    public Mono<ResponseEntity<Object>> eliminar(@PathVariable String id){
        log.info("metodo eliminar");

        return productoService.findById(id)
            .flatMap(p -> {
                return productoService.delete(p).then(Mono.just(ResponseEntity.noContent().build()));
            })
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    // 2da forma de eliminar
    @DeleteMapping("eliminar2/{id}")
    public Mono<ResponseEntity<Void>> eliminar2(@PathVariable String id){
        log.info("metodo eliminar");

        return productoService.findById(id)
            .flatMap(p -> {
                return productoService.delete(p).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
            })
            .defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }

}
