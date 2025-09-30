package com.bookhub.repository;

import com.bookhub.entity.Prestamo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PrestamoRepository {

    private static final String SQL_INSERT_PRESTAMO =
        "INSERT INTO prestamos (fecha_prestamo, fecha_devolucion, estado, usuario_id, libro_isbn) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_MARCAR_DEVUELTO =
        "UPDATE prestamos SET estado = FALSE, fecha_devolucion = ? " +
        "WHERE libro_isbn = ? AND usuario_id = ? AND estado = TRUE";
    private static final String SQL_SELECT_ACTIVOS_POR_USUARIO =
        "SELECT p.id, p.fecha_prestamo, p.fecha_devolucion, p.estado, p.usuario_id, p.libro_isbn " +
        "FROM prestamos p " +
        "INNER JOIN usuarios u ON u.id = p.usuario_id " +
        "INNER JOIN libros l ON l.isbn = p.libro_isbn " +
        "WHERE p.usuario_id = ? AND p.estado = TRUE " +
        "ORDER BY p.fecha_prestamo DESC";
    private static final String SQL_SELECT_HISTORIAL_POR_USUARIO =
        "SELECT p.id, p.fecha_prestamo, p.fecha_devolucion, p.estado, p.usuario_id, p.libro_isbn " +
        "FROM prestamos p " +
        "INNER JOIN usuarios u ON u.id = p.usuario_id " +
        "INNER JOIN libros l ON l.isbn = p.libro_isbn " +
        "WHERE p.usuario_id = ? " +
        "ORDER BY p.fecha_prestamo DESC";
    private static final String SQL_SELECT_PRESTAMOS_ACTIVOS =
        "SELECT p.id, p.fecha_prestamo, p.fecha_devolucion, p.estado, p.usuario_id, p.libro_isbn " +
        "FROM prestamos p " +
        "INNER JOIN usuarios u ON u.id = p.usuario_id " +
        "INNER JOIN libros l ON l.isbn = p.libro_isbn " +
        "WHERE p.estado = TRUE " +
        "ORDER BY p.fecha_prestamo DESC";
    private static final String SQL_SELECT_PRESTAMOS_DISPONIBLES =
        "SELECT p.id, p.fecha_prestamo, p.fecha_devolucion, p.estado, p.usuario_id, p.libro_isbn " +
        "FROM prestamos p " +
        "INNER JOIN usuarios u ON u.id = p.usuario_id " +
        "INNER JOIN libros l ON l.isbn = p.libro_isbn " +
        "WHERE p.estado = FALSE " +
        "ORDER BY p.fecha_devolucion DESC";

    private static final RowMapper<Prestamo> PRESTAMO_ROW_MAPPER = PrestamoRepository::mapRow;

    private final JdbcTemplate jdbcTemplate;

    public PrestamoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int registrarPrestamo(Prestamo prestamo) {
        return jdbcTemplate.update(
            SQL_INSERT_PRESTAMO,
            prestamo.getFechaPrestamo(),
            prestamo.getFechaDevolucion(),
            prestamo.getEstado(),
            prestamo.getUsuarioId(),
            prestamo.getLibroIsbn()
        );
    }

    public int marcarComoDevuelto(String isbn, Integer usuarioId) {
        return marcarComoDevuelto(isbn, usuarioId, LocalDate.now());
    }

    public int marcarComoDevuelto(String isbn, Integer usuarioId, LocalDate fechaDevolucionReal) {
        return jdbcTemplate.update(
            SQL_MARCAR_DEVUELTO,
            fechaDevolucionReal,
            isbn,
            usuarioId
        );
    }

    public List<Prestamo> findActivosByUsuario(Integer usuarioId) {
        return jdbcTemplate.query(SQL_SELECT_ACTIVOS_POR_USUARIO, PRESTAMO_ROW_MAPPER, usuarioId);
    }

    public List<Prestamo> findHistorialByUsuario(Integer usuarioId) {
        return jdbcTemplate.query(SQL_SELECT_HISTORIAL_POR_USUARIO, PRESTAMO_ROW_MAPPER, usuarioId);
    }

    public List<Prestamo> findPrestamosActivos() {
        return jdbcTemplate.query(SQL_SELECT_PRESTAMOS_ACTIVOS, PRESTAMO_ROW_MAPPER);
    }

    public List<Prestamo> findPrestamosDisponibles() {
        return jdbcTemplate.query(SQL_SELECT_PRESTAMOS_DISPONIBLES, PRESTAMO_ROW_MAPPER);
    }

    private static Prestamo mapRow(ResultSet rs, int rowNum) throws SQLException {
        Prestamo prestamo = new Prestamo();
        prestamo.setId(rs.getInt("id"));
        prestamo.setFechaPrestamo(rs.getObject("fecha_prestamo", LocalDate.class));
        prestamo.setFechaDevolucion(rs.getObject("fecha_devolucion", LocalDate.class));
        prestamo.setEstado(rs.getBoolean("estado"));
        prestamo.setUsuarioId(rs.getInt("usuario_id"));
        prestamo.setLibroIsbn(rs.getString("libro_isbn"));
        return prestamo;
    }
}
