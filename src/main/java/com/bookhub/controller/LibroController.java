package com.bookhub.controller;

import com.bookhub.dto.LibroEstadoRequest;
import com.bookhub.dto.LibroRequest;
import com.bookhub.dto.LibroResponse;
import com.bookhub.entity.Libro;
import com.bookhub.service.LibroService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
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
    public ResponseEntity<?> registrarLibro(@Valid @RequestBody LibroRequest request) {
        try {
            Libro creado = libroService.registrarLibro(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(libroService.toResponse(creado));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<LibroResponse>> listarLibros() {
        List<LibroResponse> respuesta = libroService.listarLibros().stream()
            .map(libroService::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<?> obtenerLibro(@PathVariable String isbn) {
        try {
            Libro libro = libroService.obtenerLibroPorIsbn(isbn);
            return ResponseEntity.ok(libroService.toResponse(libro));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<LibroResponse>> buscarLibros(@RequestParam("q") String termino) {
        List<LibroResponse> respuesta = libroService.buscarLibrosPorTituloOAutor(termino).stream()
            .map(libroService::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<LibroResponse>> listarDisponibles() {
        List<LibroResponse> respuesta = libroService.listarLibrosDisponibles().stream()
            .map(libroService::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(respuesta);
    }

    @PutMapping("/{isbn}")
    public ResponseEntity<?> actualizarLibro(@PathVariable String isbn, @Valid @RequestBody LibroRequest request) {
        try {
            Libro actualizado = libroService.actualizarLibro(isbn, request);
            return ResponseEntity.ok(libroService.toResponse(actualizado));
        } catch (IllegalArgumentException | IllegalStateException e) {
            HttpStatus status = e instanceof IllegalArgumentException ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(e.getMessage());
        }
    }

    @PatchMapping("/{isbn}/estado")
    public ResponseEntity<?> cambiarEstado(
        @PathVariable String isbn,
        @Valid @RequestBody LibroEstadoRequest request
    ) {
        try {
            Libro actualizado = libroService.cambiarEstado(isbn, request.getDisponible());
            return ResponseEntity.ok(libroService.toResponse(actualizado));
        } catch (IllegalArgumentException | IllegalStateException e) {
            HttpStatus status = e instanceof IllegalArgumentException ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(e.getMessage());
        }
    }
}
