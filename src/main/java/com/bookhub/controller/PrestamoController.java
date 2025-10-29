package com.bookhub.controller;

import com.bookhub.entity.Libro;
import com.bookhub.entity.Prestamo;
import com.bookhub.service.PrestamoService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping
    public ResponseEntity<?> prestarLibro(@RequestBody Prestamo solicitud) {
        try {
            Prestamo nuevoPrestamo = prestamoService.realizarPrestamo(
                solicitud.getUsuarioId(),
                solicitud.getLibroIsbn()
            );
            return new ResponseEntity<>(nuevoPrestamo, HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/devolver")
    public ResponseEntity<String> devolverLibro(@RequestBody Prestamo solicitud) {
        try {
            prestamoService.realizarDevolucion(
                solicitud.getUsuarioId(),
                solicitud.getLibroIsbn()
            );
            return new ResponseEntity<>("Devolucion registrada exitosamente.", HttpStatus.OK);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Prestamo>> obtenerPrestamosActivos() {
        return ResponseEntity.ok(prestamoService.consultarPrestamosActivos());
    }

    @GetMapping("/usuario/{usuarioId}/activos")
    public ResponseEntity<List<Prestamo>> obtenerActivosPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(prestamoService.consultarActivosPorUsuario(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/historial")
    public ResponseEntity<List<Prestamo>> obtenerHistorialPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(prestamoService.consultarHistorialPorUsuario(usuarioId));
    }

    @GetMapping("/libros-disponibles")
    public ResponseEntity<List<Libro>> obtenerLibrosDisponibles() {
        return ResponseEntity.ok(prestamoService.consultarLibrosDisponibles());
    }
}
