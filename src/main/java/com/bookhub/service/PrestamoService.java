package com.bookhub.service;

import com.bookhub.entity.Libro;
import com.bookhub.entity.Prestamo;
import com.bookhub.repository.PrestamoRepository;
import com.bookhub.dto.PrestamoRequest;
import com.bookhub.dto.PrestamoResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final LibroService libroService;
    private final UsuarioService usuarioService;

    public PrestamoService(PrestamoRepository prestamoRepository,
                           LibroService libroService,
                           UsuarioService usuarioService) {
        this.prestamoRepository = prestamoRepository;
        this.libroService = libroService;
        this.usuarioService = usuarioService;
    }

    // ---------------------------
    //      CREAR PRÉSTAMO
    // ---------------------------
    public Prestamo realizarPrestamo(Integer usuarioId, String libroIsbn) {

        usuarioService.validarUsuarioPuedePrestar(usuarioId);
        libroService.validarLibroDisponible(libroIsbn);

        LocalDate hoy = LocalDate.now();
        LocalDate devolucion = hoy.plusDays(15);

        Prestamo nuevo = new Prestamo();
        nuevo.setUsuarioId(usuarioId);
        nuevo.setLibroIsbn(libroIsbn);
        nuevo.setFechaPrestamo(hoy);
        nuevo.setFechaDevolucion(devolucion);
        nuevo.setEstado(true);

        // 👉 AQUÍ se reemplaza registrarPrestamo() por save()
        Prestamo guardado = prestamoRepository.save(nuevo);

        libroService.marcarComoPrestado(libroIsbn);

        return guardado;
    }

    // ---------------------------
    //      DEVOLVER PRÉSTAMO
    // ---------------------------
    public void realizarDevolucion(Integer usuarioId, String libroIsbn) {

        int filas = prestamoRepository.marcarComoDevuelto(libroIsbn, usuarioId);

        if (filas == 0) {
            throw new IllegalStateException("No existe préstamo activo para ese usuario.");
        }

        libroService.marcarComoDisponible(libroIsbn);
    }

    // ---------------------------
    //      CONSULTAS
    // ---------------------------
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

    // ---------------------------
    //   Mappers Request/Response
    // ---------------------------
    public Prestamo fromRequest(PrestamoRequest request) {
        Prestamo p = new Prestamo();
        p.setUsuarioId(request.getUsuarioId());
        p.setLibroIsbn(request.getLibroIsbn());
        p.setFechaPrestamo(request.getFechaPrestamo());
        p.setFechaDevolucion(request.getFechaDevolucion());
        p.setEstado(true);
        return p;
    }

    public PrestamoResponse toResponse(Prestamo p) {
        PrestamoResponse r = new PrestamoResponse();
        r.setId(p.getId());
        r.setUsuarioId(p.getUsuarioId());
        r.setLibroIsbn(p.getLibroIsbn());
        r.setFechaPrestamo(p.getFechaPrestamo());
        r.setFechaDevolucion(p.getFechaDevolucion());
        r.setEstado(p.getEstado());
        r.setDiasRetraso(p.calcularRetraso());
        return r;
    }
}
