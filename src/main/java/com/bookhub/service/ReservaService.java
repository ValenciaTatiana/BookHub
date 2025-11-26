package com.bookhub.service;

import com.bookhub.entity.Reserva;
import com.bookhub.repository.ReservaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final LibroService libroService;
    private final AuditoriaService auditoriaService;

    public ReservaService(ReservaRepository reservaRepository, LibroService libroService, AuditoriaService auditoriaService) {
        this.reservaRepository = reservaRepository;
        this.libroService = libroService;
        this.auditoriaService = auditoriaService;
    }

    public Reserva crearReserva(Integer usuarioId, String libroIsbn) {
        if (usuarioId == null || usuarioId <= 0) {
            throw new IllegalArgumentException("Debe indicar el usuario");
        }
        if (libroIsbn == null || libroIsbn.isBlank()) {
            throw new IllegalArgumentException("Debe indicar el ISBN del libro");
        }
        // Verificar disponibilidad; si está disponible no se reserva
        if (libroService.isLibroDisponible(libroIsbn)) {
            throw new IllegalStateException("El libro está disponible, realiza el préstamo directamente");
        }
        if (reservaRepository.existsByLibroIsbnAndUsuarioIdAndAtendidaFalse(libroIsbn, usuarioId)) {
            throw new IllegalStateException("Ya existe una reserva pendiente de este usuario para el libro");
        }
        Reserva reserva = new Reserva(usuarioId, libroIsbn);
        Reserva guardada = reservaRepository.save(reserva);
        auditoriaService.registrar(
            "RESERVA",
            "CREAR",
            guardada.getId().toString(),
            "Reserva creada para libro " + libroIsbn + " por usuario " + usuarioId
        );
        return guardada;
    }

    public List<Reserva> listarPendientes() {
        return reservaRepository.findAll()
            .stream()
            .filter(r -> Boolean.FALSE.equals(r.getAtendida()))
            .toList();
    }

    public List<Reserva> listarPendientesPorLibro(String libroIsbn) {
        return reservaRepository.findByLibroIsbnAndAtendidaFalseOrderByFechaReservaAsc(libroIsbn);
    }

    public Optional<Reserva> atenderSiguiente(String libroIsbn) {
        Optional<Reserva> siguiente = reservaRepository.findTopByLibroIsbnAndAtendidaFalseOrderByFechaReservaAsc(libroIsbn);
        siguiente.ifPresent(reserva -> {
            reserva.setAtendida(true);
            reservaRepository.save(reserva);
            auditoriaService.registrar(
                "RESERVA",
                "NOTIFICAR",
                reserva.getId().toString(),
                "Reserva atendida para libro " + libroIsbn + ", usuario " + reserva.getUsuarioId()
            );
        });
        return siguiente;
    }
}
