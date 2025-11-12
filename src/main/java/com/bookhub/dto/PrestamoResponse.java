package com.bookhub.dto;

import java.time.LocalDate;

public class PrestamoResponse {
    private Integer id;
    private Integer usuarioId;
    private String libroIsbn;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private Boolean estado;
    private Long diasRetraso;

    public PrestamoResponse() {}

    public PrestamoResponse(Integer id, Integer usuarioId, String libroIsbn,
                            LocalDate fechaPrestamo, LocalDate fechaDevolucion,
                            Boolean estado, Long diasRetraso) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.libroIsbn = libroIsbn;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = estado;
        this.diasRetraso = diasRetraso;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public String getLibroIsbn() { return libroIsbn; }
    public void setLibroIsbn(String libroIsbn) { this.libroIsbn = libroIsbn; }

    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(LocalDate fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }

    public Long getDiasRetraso() { return diasRetraso; }
    public void setDiasRetraso(Long diasRetraso) { this.diasRetraso = diasRetraso; }
}
