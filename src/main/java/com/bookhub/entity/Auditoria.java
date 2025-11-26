package com.bookhub.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria")
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String entidad;

    @Column(nullable = false)
    private String accion;

    @Column(nullable = false)
    private String referenciaId;

    @Column(nullable = false, length = 2000)
    private String detalles;

    @Column(nullable = false)
    private LocalDateTime fecha;

    public Auditoria() {
        this.fecha = LocalDateTime.now();
    }

    public Auditoria(String entidad, String accion, String referenciaId, String detalles) {
        this.entidad = entidad;
        this.accion = accion;
        this.referenciaId = referenciaId;
        this.detalles = detalles;
        this.fecha = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getReferenciaId() {
        return referenciaId;
    }

    public void setReferenciaId(String referenciaId) {
        this.referenciaId = referenciaId;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
