package com.bookhub.repository;

import com.bookhub.entity.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    @Query("SELECT p FROM Prestamo p WHERE p.usuarioId = :usuarioId AND p.estado = true")
    List<Prestamo> findActivosByUsuario(Integer usuarioId);

    @Query("SELECT p FROM Prestamo p WHERE p.usuarioId = :usuarioId AND p.estado = false")
    List<Prestamo> findHistorialByUsuario(Integer usuarioId);

    @Query("SELECT p FROM Prestamo p WHERE p.estado = true")
    List<Prestamo> findPrestamosActivos();

    @Transactional
    @Modifying
    @Query("UPDATE Prestamo p SET p.estado = false WHERE p.libroIsbn = :isbn AND p.usuarioId = :usuarioId")
    int marcarComoDevuelto(String isbn, Integer usuarioId);
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
    private static final String SQL_EXISTE_PRESTAMO_POR_LIBRO =
        "SELECT COUNT(*) FROM prestamos WHERE libro_isbn = ?";

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

    public boolean existePrestamoPorLibro(String isbn) {
        Integer count = jdbcTemplate.queryForObject(SQL_EXISTE_PRESTAMO_POR_LIBRO, Integer.class, isbn);
        return count != null && count > 0;
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
