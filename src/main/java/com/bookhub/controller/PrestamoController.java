package com.bookhub.controller;

import com.bookhub.dto.PrestamoRequest;
import com.bookhub.dto.PrestamoResponse;
import com.bookhub.entity.Libro;
import com.bookhub.service.PrestamoService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

    // Registrar un nuevo prestamo (ruta usada por el frontend)
    @PostMapping
    public ResponseEntity<?> registrarPrestamoRoot(@RequestBody PrestamoRequest request) {
        return registrarPrestamo(request);
    }

    // Registrar un nuevo prestamo (ruta legacy /registrar)
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarPrestamo(@RequestBody PrestamoRequest request) {
        try {
            var prestamo = prestamoService.realizarPrestamo(
                request.getUsuarioCedula(),
                request.getLibroIsbn()
            );
            return ResponseEntity.ok(prestamoService.toResponse(prestamo));
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    // Registrar devolucion de un libro (el frontend envia JSON en el body)
    @PutMapping("/devolver")
    public ResponseEntity<?> devolverLibro(@RequestBody PrestamoRequest request) {
        try {
            prestamoService.realizarDevolucion(request.getUsuarioCedula(), request.getLibroIsbn());
            return ResponseEntity.ok("Devolucion registrada correctamente.");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    // Consultar prestamos activos por usuario (ruta usada por el frontend)
    @GetMapping("/usuario/{usuarioCedula}/activos")
    public List<PrestamoResponse> obtenerPrestamosActivos(@PathVariable String usuarioCedula) {
        return prestamoService.consultarActivosPorUsuario(usuarioCedula)
            .stream()
            .map(prestamoService::toResponse)
            .collect(Collectors.toList());
    }

    // Consultar historial por usuario (ruta usada por el frontend)
    @GetMapping("/usuario/{usuarioCedula}/historial")
    public List<PrestamoResponse> obtenerHistorial(@PathVariable String usuarioCedula) {
        return prestamoService.consultarHistorialPorUsuario(usuarioCedula)
            .stream()
            .map(prestamoService::toResponse)
            .collect(Collectors.toList());
    }

    // Consultar todos los prestamos activos del sistema
    @GetMapping("/activos")
    public List<PrestamoResponse> obtenerTodosActivos() {
        return prestamoService.consultarPrestamosActivos()
            .stream()
            .map(prestamoService::toResponse)
            .collect(Collectors.toList());
    }

    // Consultar libros disponibles
    @GetMapping("/libros-disponibles")
    public List<Libro> obtenerLibrosDisponibles() {
        return prestamoService.consultarLibrosDisponibles();
    }
}
