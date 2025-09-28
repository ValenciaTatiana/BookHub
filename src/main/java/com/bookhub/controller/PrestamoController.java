package com.bookhub.controller;

import com.bookhub.entity.Prestamo;
import com.bookhub.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prestamos")

public class PrestamoController {

    private final PrestamoService prestamoService;

    @Autowired
    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }


    @PostMapping
    public ResponseEntity<?> prestarLibro(@RequestBody Prestamo solicitud) {
        try {
            // Llama al servicio
            Prestamo nuevoPrestamo = prestamoService.realizarPrestamo(
                    solicitud.getUsuarioId(),
                    solicitud.getLibroIsbn()
            );
            return new ResponseEntity<>(nuevoPrestamo, HttpStatus.CREATED); // 201 Created
        } catch (Exception e) {
            // Maneja excepciones de negocio
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }


    @PutMapping("/devolver")
    public ResponseEntity<String> devolverLibro(@RequestBody Prestamo solicitud) {
        try {
            prestamoService.realizarDevolucion(
                    solicitud.getUsuarioId(),
                    solicitud.getLibroIsbn()
            );
            return new ResponseEntity<>("Devolución registrada exitosamente.", HttpStatus.OK); // 200 OK
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }


    @GetMapping("/activos/{usuarioId}")
    public ResponseEntity<List<Prestamo>> getPrestamosActivos(@PathVariable Integer usuarioId) {
        List<Prestamo> activos = prestamoService.consultarActivosPorUsuario(usuarioId);
        return new ResponseEntity<>(activos, HttpStatus.OK);
    }


    @GetMapping("/historial/{usuarioId}")
    public ResponseEntity<List<Prestamo>> getHistorialPrestamos(@PathVariable Integer usuarioId) {
        List<Prestamo> historial = prestamoService.consultarHistorialPorUsuario(usuarioId);
        return new ResponseEntity<>(historial, HttpStatus.OK);
    }


}
