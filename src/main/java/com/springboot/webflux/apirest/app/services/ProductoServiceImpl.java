package com.springboot.webflux.apirest.app.services;

import org.springframework.stereotype.Service;

import com.springboot.webflux.apirest.app.models.dao.CategoriaDao;
import com.springboot.webflux.apirest.app.models.dao.ProductoDao;
import com.springboot.webflux.apirest.app.models.documents.Categoria;
import com.springboot.webflux.apirest.app.models.documents.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoServiceImpl implements ProductoService {

    // Inyectamos el ProductoDao y CategoriaDao en el constructor de la clase
    private ProductoDao productoDao;
    private CategoriaDao categoriaDao;

    public ProductoServiceImpl(ProductoDao productoDao, CategoriaDao categoriaDao) {
        this.productoDao = productoDao;
        this.categoriaDao = categoriaDao;
    }


    @Override
    public Flux<Producto> findAll() {
        
        return productoDao.findAll();
    }

    @Override
    public Mono<Producto> findById(String id) {
        
        return productoDao.findById(id);
    }

    @Override
    public Mono<Producto> save(Producto producto) {
        
        return productoDao.save(producto);
    }

    @Override
    public Mono<Void> delete(Producto producto) {
        
        return productoDao.delete(producto);
    }


    // metodos personalizados
    @Override
    public Flux<Producto> findAllNombreUpperCase() {
        
        return productoDao.findAll().map(producto -> {
                                        producto.setNombre(producto.getNombre().toUpperCase());
                                        return producto;
                                    });
    }


    @Override
    public Flux<Producto> findAllNombreUpperCaseRepeat() {
        
        return findAllNombreUpperCase().repeat(5000); // para simular un flujo de datos que llega de manera asincrona, se puede usar repeat, esto va a repetir el flujo de datos 5000 veces, para simular un flujo de datos grande
    }


    /* --------------------------- */
    @Override
    public Flux<Categoria> findAllCategoria() {
        
        return categoriaDao.findAll();
    }


    @Override
    public Mono<Categoria> findCategoriaById(String id) {
        
        return categoriaDao.findById(id);
    }


    @Override
    public Mono<Categoria> saveCategoria(Categoria categoria) {
        
        return categoriaDao.save(categoria);
    }


    @Override
    public Mono<Producto> findByNombre(String nombre) {
        
        return productoDao.buscarNombre(nombre);
    }


    @Override
    public Mono<Categoria> findCategoriaByNombre(String nombre) {
        
        return categoriaDao.findByNombre(nombre);
    }
    

}
