package com.bookhub.entity;
import java.time.LocalDate;
public class Prestamo {

        private Integer id; // PK
        private LocalDate fechaPrestamo; // Fecha en que se prestó (Date)
        private LocalDate fechaDevolucion; // Fecha esperada de devolución (Date)
        private Boolean estado; // True = Activo, False = Devuelto
        private Integer usuarioId; // FK (ID de la tabla usuarios)
        private String libroIsbn; // FK (ISBN de la tabla libros)

        // Constructor por defecto
        public Prestamo() {
        }

        // Constructor con parámetros
        public Prestamo(Integer id, LocalDate fechaPrestamo, LocalDate fechaDevolucion, Boolean estado, Integer usuarioId, String libroIsbn) {
            this.id = id;
            this.fechaPrestamo = fechaPrestamo;
            this.fechaDevolucion = fechaDevolucion;
            this.estado = estado;
            this.usuarioId = usuarioId;
            this.libroIsbn = libroIsbn;
        }

        // Método Calcular el retraso
        public long calcularRetraso() {
            // Solo calcular si el estado es ACTIVO (true) o si la fecha ya pasó
            if (this.estado != null && !this.estado) {
                return 0; // Ya fue devuelto
            }

            LocalDate hoy = LocalDate.now();
            if (hoy.isAfter(this.fechaDevolucion)) {
                // Retraso en días
                return hoy.toEpochDay() - this.fechaDevolucion.toEpochDay();
            }
            return 0; // No hay retraso
        }

        // Getters y Setters
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public LocalDate getFechaPrestamo() { return fechaPrestamo; }
        public void setFechaPrestamo(LocalDate fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }
        public LocalDate getFechaDevolucion() { return fechaDevolucion; }
        public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }
        public Boolean getEstado() { return estado; }
        public void setEstado(Boolean estado) { this.estado = estado; }
        public Integer getUsuarioId() { return usuarioId; }
        public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }
        public String getLibroIsbn() { return libroIsbn; }
        public void setLibroIsbn(String libroIsbn) { this.libroIsbn = libroIsbn; }
    }

