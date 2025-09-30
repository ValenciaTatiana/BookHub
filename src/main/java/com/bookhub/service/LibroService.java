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

    public Libro registrarLibro(Libro libro) {
        if (libro == null || libro.getIsbn() == null || libro.getIsbn().isBlank()) {
            throw new IllegalArgumentException("Debe especificar un ISBN para registrar el libro");
        }
        if (libroRepository.findByIsbn(libro.getIsbn()).isPresent()) {
            throw new IllegalStateException("Ya existe un libro con el ISBN " + libro.getIsbn());
        }
        libroRepository.registrar(libro);
        return libroRepository.findByIsbn(libro.getIsbn()).orElse(libro);
    }

    public List<Libro> listarLibros() {
        return libroRepository.findAll();
    }

    public List<Libro> listarLibrosDisponibles() {
        return libroRepository.findDisponibles();
    }

    public Libro obtenerLibroPorIsbn(String isbn) {
        return libroRepository.findByIsbn(isbn)
            .orElseThrow(() -> new IllegalArgumentException("No existe libro con ISBN " + isbn));
    }

    public List<Libro> buscarLibrosPorTituloOAutor(String termino) {
        return libroRepository.buscarPorTituloOAutor(termino);
    }

    public Libro actualizarLibro(Libro libro) {
        if (libro == null || libro.getIsbn() == null || libro.getIsbn().isBlank()) {
            throw new IllegalArgumentException("Debe especificar el ISBN del libro a actualizar");
        }
        Libro existente = obtenerLibroPorIsbn(libro.getIsbn());
        existente.setTitulo(libro.getTitulo());
        existente.setAutor(libro.getAutor());
        existente.setCategoria(libro.getCategoria());
        existente.setDisponible(libro.isDisponible());
        libroRepository.actualizar(existente);
        return existente;
    }

    public Libro prestarLibro(String isbn) {
        Libro libro = validarLibroDisponible(isbn);
        libro.prestar();
        persistirEstado(libro, libro.isDisponible());
        return libro;
    }

    public Libro devolverLibro(String isbn) {
        Libro libro = obtenerLibroPorIsbn(isbn);
        if (libro.isDisponible()) {
            return libro;
        }
        libro.devolver();
        persistirEstado(libro, libro.isDisponible());
        return libro;
    }

    public void marcarComoPrestado(String isbn) {
        prestarLibro(isbn);
    }

    public void marcarComoDisponible(String isbn) {
        Libro libro = obtenerLibroPorIsbn(isbn);
        libro.devolver();
        persistirEstado(libro, true);
    }

    public boolean isLibroDisponible(String isbn) {
        return libroRepository.findByIsbn(isbn).map(Libro::isDisponible).orElse(false);
    }

    public Libro validarLibroDisponible(String isbn) {
        Libro libro = obtenerLibroPorIsbn(isbn);
        if (!libro.isDisponible()) {
            throw new IllegalStateException("RN002: El libro con ISBN " + isbn + " no esta disponible");
        }
        return libro;
    }

    public Libro cambiarEstado(String isbn, boolean disponible) {
        Libro libro = obtenerLibroPorIsbn(isbn);
        persistirEstado(libro, disponible);
        return libro;
    }

    private void persistirEstado(Libro libro, boolean disponible) {
        int filas = libroRepository.updateDisponibilidad(libro.getIsbn(), disponible);
        if (filas == 0) {
            throw new IllegalStateException("No se pudo actualizar el estado del libro " + libro.getIsbn());
        }
        libro.setDisponible(disponible);
    }
}
