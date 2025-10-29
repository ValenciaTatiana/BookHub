package com.bookhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LibroRequest {

    @Size(max = 20, message = "El ISBN no debe superar los 20 caracteres")
    private String isbn;

    @NotBlank(message = "El titulo es obligatorio")
    @Size(max = 100, message = "El titulo no debe superar los 100 caracteres")
    private String titulo;

    @NotBlank(message = "El autor es obligatorio")
    @Size(max = 100, message = "El autor no debe superar los 100 caracteres")
    private String autor;

    @NotBlank(message = "La categoria es obligatoria")
    @Size(max = 50, message = "La categoria no debe superar los 50 caracteres")
    private String categoria;

    private Boolean disponible;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
}
