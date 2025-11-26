package com.bookhub.service;

import com.bookhub.dto.PrestamoRequest;
import com.bookhub.dto.PrestamoResponse;
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
    private final ReservaService reservaService;
    private final AuditoriaService auditoriaService;

    public PrestamoService(PrestamoRepository prestamoRepository,
                           LibroService libroService,
                           UsuarioService usuarioService,
                           ReservaService reservaService,
                           AuditoriaService auditoriaService) {
        this.prestamoRepository = prestamoRepository;
        this.libroService = libroService;
        this.usuarioService = usuarioService;
        this.reservaService = reservaService;
        this.auditoriaService = auditoriaService;
    }

    // ---------------------------
    //      CREAR PRESTAMO
    // ---------------------------
    public Prestamo realizarPrestamo(String usuarioCedula, String libroIsbn) {

        Integer usuarioId = obtenerUsuarioIdPorCedula(usuarioCedula);
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

        Prestamo guardado = prestamoRepository.save(nuevo);

        libroService.marcarComoPrestado(libroIsbn);
        auditoriaService.registrar(
            "PRESTAMO",
            "CREAR",
            guardado.getId() != null ? guardado.getId().toString() : "N/A",
            "Prestamo creado: usuario " + usuarioCedula + ", libro " + libroIsbn
        );

        return guardado;
    }

    // ---------------------------
    //      DEVOLVER PRESTAMO
    // ---------------------------
    public void realizarDevolucion(String usuarioCedula, String libroIsbn) {

        Integer usuarioId = obtenerUsuarioIdPorCedula(usuarioCedula);
        int filas = prestamoRepository.marcarComoDevuelto(libroIsbn, usuarioId);

        if (filas == 0) {
            throw new IllegalStateException("No existe prestamo activo para ese usuario.");
        }

        libroService.marcarComoDisponible(libroIsbn);
        auditoriaService.registrar(
            "PRESTAMO",
            "DEVOLVER",
            libroIsbn,
            "Devolucion registrada para usuario " + usuarioCedula + ", libro " + libroIsbn
        );
        reservaService.atenderSiguiente(libroIsbn);
    }

    // ---------------------------
    //      CONSULTAS
    // ---------------------------
    public List<Prestamo> consultarActivosPorUsuario(String usuarioCedula) {
        Integer usuarioId = obtenerUsuarioIdPorCedula(usuarioCedula);
        return prestamoRepository.findActivosByUsuario(usuarioId);
    }

    public List<Prestamo> consultarHistorialPorUsuario(String usuarioCedula) {
        Integer usuarioId = obtenerUsuarioIdPorCedula(usuarioCedula);
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
        p.setUsuarioId(obtenerUsuarioIdPorCedula(request.getUsuarioCedula()));
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
        r.setUsuarioCedula(obtenerCedulaUsuario(p.getUsuarioId()));
        r.setLibroIsbn(p.getLibroIsbn());
        r.setFechaPrestamo(p.getFechaPrestamo());
        r.setFechaDevolucion(p.getFechaDevolucion());
        r.setEstado(p.getEstado());
        r.setDiasRetraso(p.calcularRetraso());
        return r;
    }

    private Integer obtenerUsuarioIdPorCedula(String cedula) {
        if (cedula == null || cedula.isBlank()) {
            throw new IllegalArgumentException("La cedula del usuario es obligatoria.");
        }
        return usuarioService.obtenerUsuarioPorCedula(cedula).getId();
    }

    private String obtenerCedulaUsuario(Integer usuarioId) {
        if (usuarioId == null) {
            return null;
        }
        try {
            return usuarioService.obtenerUsuarioPorId(usuarioId).getCedula();
        } catch (RuntimeException ex) {
            return null;
        }
    }
}