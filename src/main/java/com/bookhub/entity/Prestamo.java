package com.bookhub.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "prestamos")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer usuarioId;

    @Column(nullable = false)
    private String libroIsbn;

    @Column(nullable = false)
    private LocalDate fechaPrestamo;

    @Column(nullable = false)
    private LocalDate fechaDevolucion;

    @Column(nullable = false)
    private Boolean estado;

    public Prestamo() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    // usado por PrestamoResponse
    public int calcularRetraso() {
        if (fechaDevolucion == null) return 0;
        if (fechaDevolucion.isBefore(LocalDate.now())) {
            return fechaDevolucion.until(LocalDate.now()).getDays();
        }
        return 0;
    }
}
