package com.bookhub.repository;

import com.bookhub.entity.Libro;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class LibroRepository {

    private static final RowMapper<Libro> LIBRO_ROW_MAPPER = (rs, rowNum) -> {
        Libro libro = new Libro();
        libro.setIsbn(rs.getString("isbn"));
        libro.setTitulo(rs.getString("titulo"));
        libro.setAutor(rs.getString("autor"));
        libro.setCategoria(rs.getString("categoria"));
        libro.setDisponible(rs.getBoolean("estado"));
        return libro;
    };

    private final JdbcTemplate jdbcTemplate;

    public LibroRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Libro> findAll() {
        String sql = "SELECT isbn, titulo, autor, categoria, estado FROM libros ORDER BY titulo";
        return jdbcTemplate.query(sql, LIBRO_ROW_MAPPER);
    }

    public Optional<Libro> findByIsbn(String isbn) {
        String sql = "SELECT isbn, titulo, autor, categoria, estado FROM libros WHERE isbn = ?";
        return jdbcTemplate.query(sql, LIBRO_ROW_MAPPER, isbn).stream().findFirst();
    }

    public int updateDisponibilidad(String isbn, boolean disponible) {
        String sql = "UPDATE libros SET estado = ? WHERE isbn = ?";
        return jdbcTemplate.update(sql, disponible, isbn);
    }
}
