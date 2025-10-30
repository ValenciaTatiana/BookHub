package com.bookhub.dto;

import java.time.LocalDate;

public class PrestamoRequest {
    private Integer usuarioId;
    private String libroIsbn;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;

    public PrestamoRequest() {}

    public PrestamoRequest(Integer usuarioId, String libroIsbn,
                           LocalDate fechaPrestamo, LocalDate fechaDevolucion) {
        this.usuarioId = usuarioId;
        this.libroIsbn = libroIsbn;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
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
}

