package com.bookhub.service;

import com.bookhub.dto.LibroRequest;
import com.bookhub.dto.LibroResponse;
import com.bookhub.entity.Libro;
import com.bookhub.repository.LibroRepository;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class LibroService {

    private final LibroRepository libroRepository;

    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Transactional
    public Libro registrarLibro(LibroRequest request) {
        return registrarLibro(fromRequest(request));
    }

    @Transactional
    public Libro registrarLibro(Libro libro) {
        if (libro == null || libro.getIsbn() == null || libro.getIsbn().isBlank()) {
            throw new IllegalArgumentException("Debe especificar un ISBN para registrar el libro");
        }
        if (libroRepository.existsById(libro.getIsbn())) {
            throw new IllegalStateException("Ya existe un libro con el ISBN " + libro.getIsbn());
        }
        return libroRepository.save(libro);
    }

    public List<Libro> listarLibros() {
        return libroRepository.findAllByOrderByTituloAsc();
    }

    public List<Libro> listarLibrosDisponibles() {
        return libroRepository.findByDisponibleTrueOrderByTituloAsc();
    }

    public Libro obtenerLibroPorIsbn(String isbn) {
        return libroRepository.findById(isbn)
            .orElseThrow(() -> new IllegalArgumentException("No existe libro con ISBN " + isbn));
    }

    public List<Libro> buscarLibrosPorTituloOAutor(String termino) {
        if (termino == null || termino.isBlank()) {
            return listarLibros();
        }
        return libroRepository.buscarPorTituloOAutor(termino);
    }

    @Transactional
    public Libro actualizarLibro(String isbn, LibroRequest request) {
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("Debe especificar el ISBN del libro a actualizar");
        }
        Libro existente = obtenerLibroPorIsbn(isbn);
        merge(existente, request);
        return libroRepository.save(existente);
    }

    @Transactional
    public Libro prestarLibro(String isbn) {
        Libro libro = validarLibroDisponible(isbn);
        libro.prestar();
        return libroRepository.save(libro);
    }

    @Transactional
    public Libro devolverLibro(String isbn) {
        Libro libro = obtenerLibroPorIsbn(isbn);
        if (libro.isDisponible()) {
            return libro;
        }
        libro.devolver();
        return libroRepository.save(libro);
    }

    @Transactional
    public void marcarComoPrestado(String isbn) {
        prestarLibro(isbn);
    }

    @Transactional
    public void marcarComoDisponible(String isbn) {
        Libro libro = obtenerLibroPorIsbn(isbn);
        libro.devolver();
        libroRepository.save(libro);
    }

    public boolean isLibroDisponible(String isbn) {
        return libroRepository.findById(isbn).map(Libro::isDisponible).orElse(false);
    }

    public Libro validarLibroDisponible(String isbn) {
        Libro libro = obtenerLibroPorIsbn(isbn);
        if (!libro.isDisponible()) {
            throw new IllegalStateException("RN002: El libro con ISBN " + isbn + " no esta disponible");
        }
        return libro;
    }

    @Transactional
    public Libro cambiarEstado(String isbn, boolean disponible) {
        Libro libro = obtenerLibroPorIsbn(isbn);
        libro.setDisponible(disponible);
        return libroRepository.save(libro);
    }

    public LibroResponse toResponse(Libro libro) {
        if (libro == null) {
            return null;
        }
        return new LibroResponse(
            libro.getIsbn(),
            libro.getTitulo(),
            libro.getAutor(),
            libro.getCategoria(),
            libro.isDisponible()
        );
    }

    public Libro fromRequest(LibroRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud de libro no puede ser nula");
        }
        Libro libro = new Libro();
        libro.setIsbn(request.getIsbn());
        libro.setTitulo(request.getTitulo());
        libro.setAutor(request.getAutor());
        libro.setCategoria(request.getCategoria());
        libro.setDisponible(request.getDisponible() != null ? request.getDisponible() : true);
        return libro;
    }

    public void merge(Libro destino, LibroRequest request) {
        if (destino == null || request == null) {
            throw new IllegalArgumentException("Los datos para actualizar el libro son invalidos");
        }
        destino.setTitulo(request.getTitulo());
        destino.setAutor(request.getAutor());
        destino.setCategoria(request.getCategoria());
        if (request.getDisponible() != null) {
            destino.setDisponible(request.getDisponible());
        }
    }
}
