package com.bookhub.controller;

import com.bookhub.entity.Reserva;
import com.bookhub.service.ReservaService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public Reserva crear(@RequestParam Integer usuarioId, @RequestParam String libroIsbn) {
        return reservaService.crearReserva(usuarioId, libroIsbn);
    }

    @GetMapping("/pendientes")
    public List<Reserva> pendientes(@RequestParam(required = false) String libroIsbn) {
        if (libroIsbn != null && !libroIsbn.isBlank()) {
            return reservaService.listarPendientesPorLibro(libroIsbn);
        }
        return reservaService.listarPendientes();
    }
}
