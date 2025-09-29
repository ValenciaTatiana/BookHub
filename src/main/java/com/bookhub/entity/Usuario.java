package com.bookhub.entity;

public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private String telefono;

    public Usuario() {
    }

    public Usuario(int id, String nombre, String email, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // Método extra: validación de préstamos activos
    public boolean puedePrestar(int prestamosActivos) {
        return prestamosActivos < 3; // Un usuario no puede tener más de 3 préstamos
    }
}
