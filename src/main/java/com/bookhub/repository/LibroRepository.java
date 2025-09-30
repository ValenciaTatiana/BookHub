package com.bookhub.repository;

import com.bookhub.entity.Libro;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class LibroRepository {

    private static final String SQL_INSERT_LIBRO =
        "INSERT INTO libros (isbn, titulo, autor, categoria, estado) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_TODOS =
        "SELECT isbn, titulo, autor, categoria, estado FROM libros ORDER BY titulo";
    private static final String SQL_SELECT_POR_ISBN =
        "SELECT isbn, titulo, autor, categoria, estado FROM libros WHERE isbn = ?";
    private static final String SQL_BUSCAR_POR_TITULO_O_AUTOR =
        "SELECT isbn, titulo, autor, categoria, estado FROM libros " +
        "WHERE LOWER(titulo) LIKE LOWER(?) OR LOWER(autor) LIKE LOWER(?) ORDER BY titulo";
    private static final String SQL_SELECT_DISPONIBLES =
        "SELECT isbn, titulo, autor, categoria, estado FROM libros WHERE estado = TRUE ORDER BY titulo";
    private static final String SQL_ACTUALIZAR_LIBRO =
        "UPDATE libros SET titulo = ?, autor = ?, categoria = ?, estado = ? WHERE isbn = ?";
    private static final String SQL_ACTUALIZAR_ESTADO =
        "UPDATE libros SET estado = ? WHERE isbn = ?";

    private static final RowMapper<Libro> LIBRO_ROW_MAPPER = LibroRepository::mapRow;

    private final JdbcTemplate jdbcTemplate;

    public LibroRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int registrar(Libro libro) {
        return jdbcTemplate.update(
            SQL_INSERT_LIBRO,
            libro.getIsbn(),
            libro.getTitulo(),
            libro.getAutor(),
            libro.getCategoria(),
            libro.isDisponible()
        );
    }

    public List<Libro> findAll() {
        return jdbcTemplate.query(SQL_SELECT_TODOS, LIBRO_ROW_MAPPER);
    }

    public Optional<Libro> findByIsbn(String isbn) {
        List<Libro> resultados = jdbcTemplate.query(SQL_SELECT_POR_ISBN, LIBRO_ROW_MAPPER, isbn);
        return resultados.stream().findFirst();
    }

    public List<Libro> buscarPorTituloOAutor(String termino) {
        String criterio = "%" + termino + "%";
        return jdbcTemplate.query(
            SQL_BUSCAR_POR_TITULO_O_AUTOR,
            LIBRO_ROW_MAPPER,
            criterio,
            criterio
        );
    }

    public List<Libro> findDisponibles() {
        return jdbcTemplate.query(SQL_SELECT_DISPONIBLES, LIBRO_ROW_MAPPER);
    }

    public int actualizar(Libro libro) {
        return jdbcTemplate.update(
            SQL_ACTUALIZAR_LIBRO,
            libro.getTitulo(),
            libro.getAutor(),
            libro.getCategoria(),
            libro.isDisponible(),
            libro.getIsbn()
        );
    }

    public int updateDisponibilidad(String isbn, boolean disponible) {
        return jdbcTemplate.update(SQL_ACTUALIZAR_ESTADO, disponible, isbn);
    }

    private static Libro mapRow(ResultSet rs, int rowNum) throws SQLException {
        Libro libro = new Libro();
        libro.setIsbn(rs.getString("isbn"));
        libro.setTitulo(rs.getString("titulo"));
        libro.setAutor(rs.getString("autor"));
        libro.setCategoria(rs.getString("categoria"));
        libro.setDisponible(rs.getBoolean("estado"));
        return libro;
    }
}
