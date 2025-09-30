package com.bookhub.service;

import com.bookhub.entity.Libro;
import com.bookhub.entity.Prestamo;
import com.bookhub.repository.PrestamoRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final LibroService libroService;
    private final UsuarioService usuarioService;

    public PrestamoService(PrestamoRepository prestamoRepository, LibroService libroService, UsuarioService usuarioService) {
        this.prestamoRepository = prestamoRepository;
        this.libroService = libroService;
        this.usuarioService = usuarioService;
    }

    public Prestamo realizarPrestamo(Integer usuarioId, String libroIsbn) {
        usuarioService.validarUsuarioPuedePrestar(usuarioId);
        libroService.validarLibroDisponible(libroIsbn);

        LocalDate fechaPrestamo = LocalDate.now();
        LocalDate fechaDevolucion = fechaPrestamo.plusDays(15);

        Prestamo nuevoPrestamo = new Prestamo();
        nuevoPrestamo.setUsuarioId(usuarioId);
        nuevoPrestamo.setLibroIsbn(libroIsbn);
        nuevoPrestamo.setFechaPrestamo(fechaPrestamo);
        nuevoPrestamo.setFechaDevolucion(fechaDevolucion);
        nuevoPrestamo.setEstado(true);

        int filas = prestamoRepository.registrarPrestamo(nuevoPrestamo);
        if (filas == 0) {
            throw new IllegalStateException("No se pudo registrar el prestamo para el libro " + libroIsbn);
        }

        libroService.marcarComoPrestado(libroIsbn);
        return nuevoPrestamo;
    }

    public void realizarDevolucion(Integer usuarioId, String libroIsbn) {
        int filasAfectadas = prestamoRepository.marcarComoDevuelto(libroIsbn, usuarioId);
        if (filasAfectadas == 0) {
            throw new IllegalStateException("No se encontro prestamo activo para el usuario " + usuarioId);
        }
        libroService.marcarComoDisponible(libroIsbn);
    }

    public List<Prestamo> consultarActivosPorUsuario(Integer usuarioId) {
        return prestamoRepository.findActivosByUsuario(usuarioId);
    }

    public List<Prestamo> consultarHistorialPorUsuario(Integer usuarioId) {
        return prestamoRepository.findHistorialByUsuario(usuarioId);
    }

    public List<Prestamo> consultarPrestamosActivos() {
        return prestamoRepository.findPrestamosActivos();
    }

    public List<Libro> consultarLibrosDisponibles() {
        return libroService.listarLibrosDisponibles();
    }
}
