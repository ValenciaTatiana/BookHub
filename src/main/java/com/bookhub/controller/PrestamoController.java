package com.bookhub.controller;

import com.bookhub.dto.PrestamoRequest;
import com.bookhub.dto.PrestamoResponse;
import com.bookhub.entity.Libro;
import com.bookhub.service.PrestamoService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    // Registrar un nuevo préstamo (ruta usada por el frontend)
    @PostMapping
    public PrestamoResponse registrarPrestamoRoot(@RequestBody PrestamoRequest request) {
        return registrarPrestamo(request);
    }

    // Registrar un nuevo préstamo (ruta legacy /registrar)
    @PostMapping("/registrar")
    public PrestamoResponse registrarPrestamo(@RequestBody PrestamoRequest request) {
        var prestamo = prestamoService.realizarPrestamo(
            request.getUsuarioId(),
            request.getLibroIsbn()
        );
        return prestamoService.toResponse(prestamo);
    }

    // Registrar devolución de un libro (el frontend envía JSON en el body)
    @PutMapping("/devolver")
    public String devolverLibro(@RequestBody PrestamoRequest request) {
        prestamoService.realizarDevolucion(request.getUsuarioId(), request.getLibroIsbn());
        return "Devolución registrada correctamente.";
    }

    // Consultar préstamos activos por usuario (ruta usada por el frontend)
    @GetMapping("/usuario/{usuarioId}/activos")
    public List<PrestamoResponse> obtenerPrestamosActivos(@PathVariable Integer usuarioId) {
        return prestamoService.consultarActivosPorUsuario(usuarioId)
            .stream()
            .map(prestamoService::toResponse)
            .collect(Collectors.toList());
    }

    // Consultar historial por usuario (ruta usada por el frontend)
    @GetMapping("/usuario/{usuarioId}/historial")
    public List<PrestamoResponse> obtenerHistorial(@PathVariable Integer usuarioId) {
        return prestamoService.consultarHistorialPorUsuario(usuarioId)
            .stream()
            .map(prestamoService::toResponse)
            .collect(Collectors.toList());
    }

    // Consultar todos los préstamos activos del sistema
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
