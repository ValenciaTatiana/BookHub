package com.bookhub.controller;

import com.bookhub.entity.Libro;
import com.bookhub.service.LibroService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping
    public List<Libro> listarLibros() {
        return libroService.listarLibros();
    }

    @PostMapping("/{isbn}/prestar")
    public ResponseEntity<?> prestarLibro(@PathVariable String isbn) {
        try {
            Libro libro = libroService.prestarLibro(isbn);
            return ResponseEntity.ok(libro);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/{isbn}/devolver")
    public ResponseEntity<?> devolverLibro(@PathVariable String isbn) {
        try {
            Libro libro = libroService.devolverLibro(isbn);
            return ResponseEntity.ok(libro);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
