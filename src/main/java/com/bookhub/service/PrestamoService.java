package com.bookhub.service;

import com.bookhub.entity.Prestamo;
import com.bookhub.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoService {

    // Dependencias
    private final PrestamoRepository prestamoRepository;

    private final LibroService libroService;
    private final UsuarioService usuarioService;

    @Autowired
    // Inyección de dependencias para el Service (Paso 4)
    public PrestamoService(PrestamoRepository prestamoRepository, LibroService libroService, UsuarioService usuarioService) {
        this.prestamoRepository = prestamoRepository;
        this.libroService = libroService;
        this.usuarioService = usuarioService;
    }


    public Prestamo realizarPrestamo(Integer usuarioId, String libroIsbn) throws Exception {
        // 1. RN001: Chequear que el usuario no tenga más de 3 préstamos activos
        if (prestamoRepository.findActivosByUsuario(usuarioId).size() >= 3) {
            throw new Exception("RN001: El usuario ya tiene el máximo de 3 préstamos activos.");
        }


        if (!libroService.isLibroDisponible(libroIsbn)) {
            throw new Exception("RN002: El libro no está disponible para préstamo.");
        }

        // Calcular fecha de devolución (15 días)
        LocalDate fechaPrestamo = LocalDate.now();
        LocalDate fechaDevolucion = fechaPrestamo.plusDays(15);

        Prestamo nuevoPrestamo = new Prestamo();
        nuevoPrestamo.setUsuarioId(usuarioId);
        nuevoPrestamo.setLibroIsbn(libroIsbn);
        nuevoPrestamo.setFechaPrestamo(fechaPrestamo);
        nuevoPrestamo.setFechaDevolucion(fechaDevolucion);
        nuevoPrestamo.setEstado(true); // Activo

        // 4. Registrar en la BD
        prestamoRepository.registrarPrestamo(nuevoPrestamo);

        // 5. Actualizar estado del libro (usa el servicio de Angie)
        libroService.marcarComoPrestado(libroIsbn);

        return nuevoPrestamo;
    }


    public void realizarDevolucion(Integer usuarioId, String libroIsbn) throws Exception {
        int filasAfectadas = prestamoRepository.marcarComoDevuelto(libroIsbn, usuarioId);

        if (filasAfectadas == 0) {
            throw new Exception("No se encontró préstamo activo para el usuario y el libro especificados.");
        }

        // Actualizar el estado del libro (usa el servicio de Angie)
        libroService.marcarComoDisponible(libroIsbn);
    }

    //  Consultar préstamos activos
    public List<Prestamo> consultarActivosPorUsuario(Integer usuarioId) {
        return prestamoRepository.findActivosByUsuario(usuarioId);
    }

    // Consultar historial de préstamos
    public List<Prestamo> consultarHistorialPorUsuario(Integer usuarioId) {
        return prestamoRepository.findHistorialByUsuario(usuarioId);
    }


    @Service
    public static class LibroService {
        public boolean isLibroDisponible(String isbn) { return true; }
        public void marcarComoPrestado(String isbn) { /* Lógica de Angie */ }
        public void marcarComoDisponible(String isbn) { /* Lógica de Angie */ }
    }
    @Service
    public static class UsuarioService {

    }
}
