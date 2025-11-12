package com.bookhub.controller;

import com.bookhub.dto.PrestamoRequest;
import com.bookhub.dto.PrestamoResponse;
import com.bookhub.entity.Libro;
import com.bookhub.entity.Prestamo;
import com.bookhub.service.PrestamoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    // 🔹 Registrar un nuevo préstamo
    @PostMapping("/registrar")
    public PrestamoResponse registrarPrestamo(@RequestBody PrestamoRequest request) {
        Prestamo prestamo = prestamoService.realizarPrestamo(
                request.getUsuarioId(),
                request.getLibroIsbn()
        );
        return prestamoService.toResponse(prestamo);
    }

    // 🔹 Registrar devolución de un libro
    @PutMapping("/devolver")
    public String devolverLibro(@RequestParam Integer usuarioId, @RequestParam String libroIsbn) {
        prestamoService.realizarDevolucion(usuarioId, libroIsbn);
        return "Devolución registrada correctamente.";
    }

    // 🔹 Consultar préstamos activos por usuario
    @GetMapping("/activos/{usuarioId}")
    public List<PrestamoResponse> obtenerPrestamosActivos(@PathVariable Integer usuarioId) {
        return prestamoService.consultarActivosPorUsuario(usuarioId)
                .stream()
                .map(prestamoService::toResponse)
                .collect(Collectors.toList());
    }

    // 🔹 Consultar historial por usuario
    @GetMapping("/historial/{usuarioId}")
    public List<PrestamoResponse> obtenerHistorial(@PathVariable Integer usuarioId) {
        return prestamoService.consultarHistorialPorUsuario(usuarioId)
                .stream()
                .map(prestamoService::toResponse)
                .collect(Collectors.toList());
    }

    // 🔹 Consultar todos los préstamos activos del sistema
    @GetMapping("/activos")
    public List<PrestamoResponse> obtenerTodosActivos() {
        return prestamoService.consultarPrestamosActivos()
                .stream()
                .map(prestamoService::toResponse)
                .collect(Collectors.toList());
    }

    // 🔹 Consultar libros disponibles
    @GetMapping("/libros-disponibles")
    public List<Libro> obtenerLibrosDisponibles() {
        return prestamoService.consultarLibrosDisponibles();
    }
}

