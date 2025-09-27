package com.bookhub.service;

import com.bookhub.entity.Libro;
import com.bookhub.repository.LibroRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LibroService {

    private final LibroRepository libroRepository;

    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public List<Libro> listarLibros() {
        return libroRepository.findAll();
    }

    public Libro prestarLibro(String isbn) {
        Libro libro = libroRepository.findByIsbn(isbn)
            .orElseThrow(() -> new IllegalArgumentException("No existe libro con ISBN " + isbn));
        libro.prestar();
        int updated = libroRepository.updateDisponibilidad(isbn, false);
        if (updated == 0) {
            throw new IllegalStateException("No se pudo actualizar el estado del libro " + isbn);
        }
        return libro;
    }

    public Libro devolverLibro(String isbn) {
        Libro libro = libroRepository.findByIsbn(isbn)
            .orElseThrow(() -> new IllegalArgumentException("No existe libro con ISBN " + isbn));
        libro.devolver();
        int updated = libroRepository.updateDisponibilidad(isbn, true);
        if (updated == 0) {
            throw new IllegalStateException("No se pudo actualizar el estado del libro " + isbn);
        }
        return libro;
    }
}
