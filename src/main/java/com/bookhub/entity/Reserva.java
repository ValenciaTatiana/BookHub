package com.bookhub.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer usuarioId;

    @Column(nullable = false)
    private String libroIsbn;

    @Column(nullable = false)
    private LocalDateTime fechaReserva;

    @Column(nullable = false)
    private Boolean atendida;

    public Reserva() {
        this.fechaReserva = LocalDateTime.now();
        this.atendida = false;
    }

    public Reserva(Integer usuarioId, String libroIsbn) {
        this.usuarioId = usuarioId;
        this.libroIsbn = libroIsbn;
        this.fechaReserva = LocalDateTime.now();
        this.atendida = false;
    }

    public Long getId() {
        return id;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getLibroIsbn() {
        return libroIsbn;
    }

    public void setLibroIsbn(String libroIsbn) {
        this.libroIsbn = libroIsbn;
    }

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public Boolean getAtendida() {
        return atendida;
    }

    public void setAtendida(Boolean atendida) {
        this.atendida = atendida;
    }
}
