package com.bookhub.dto;

import java.time.LocalDate;

public class PrestamoRequest {
    private String usuarioCedula;
    private String libroIsbn;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;

    public PrestamoRequest() {}

    public PrestamoRequest(String usuarioCedula, String libroIsbn,
                           LocalDate fechaPrestamo, LocalDate fechaDevolucion) {
        this.usuarioCedula = usuarioCedula;
        this.libroIsbn = libroIsbn;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getUsuarioCedula() {
        return usuarioCedula;
    }

    public void setUsuarioCedula(String usuarioCedula) {
        this.usuarioCedula = usuarioCedula;
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

