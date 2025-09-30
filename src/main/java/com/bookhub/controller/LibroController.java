package com.bookhub.controller;

import com.bookhub.entity.Libro;
import com.bookhub.service.LibroService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @PostMapping
    public ResponseEntity<?> registrarLibro(@RequestBody Libro libro) {
        try {
            Libro creado = libroService.registrarLibro(libro);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Libro>> listarLibros() {
        return ResponseEntity.ok(libroService.listarLibros());
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<?> obtenerLibro(@PathVariable String isbn) {
        try {
            return ResponseEntity.ok(libroService.obtenerLibroPorIsbn(isbn));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Libro>> buscarLibros(@RequestParam("q") String termino) {
        return ResponseEntity.ok(libroService.buscarLibrosPorTituloOAutor(termino));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Libro>> listarDisponibles() {
        return ResponseEntity.ok(libroService.listarLibrosDisponibles());
    }

    @PutMapping("/{isbn}")
    public ResponseEntity<?> actualizarLibro(@PathVariable String isbn, @RequestBody Libro libro) {
        try {
            libro.setIsbn(isbn);
            return ResponseEntity.ok(libroService.actualizarLibro(libro));
        } catch (IllegalArgumentException | IllegalStateException e) {
            HttpStatus status = e instanceof IllegalArgumentException ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(e.getMessage());
        }
    }

    @PatchMapping("/{isbn}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable String isbn, @RequestParam("disponible") boolean disponible) {
        try {
            return ResponseEntity.ok(libroService.cambiarEstado(isbn, disponible));
        } catch (IllegalArgumentException | IllegalStateException e) {
            HttpStatus status = e instanceof IllegalArgumentException ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(e.getMessage());
        }
    }
}
