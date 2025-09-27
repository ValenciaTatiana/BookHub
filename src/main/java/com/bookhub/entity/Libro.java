package com.bookhub.entity;

public class Libro {
    private String isbn;
    private String titulo;
    private String autor;
    private String categoria;
    private boolean disponible = true;

    public Libro() {
    }

    public Libro(String isbn, String titulo, String autor, String categoria, boolean disponible) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.disponible = disponible;
    }

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

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public void prestar() {
        if (!disponible) {
            throw new IllegalStateException("El libro ya esta prestado");
        }
        disponible = false;
    }

    public void devolver() {
        disponible = true;
    }
}
