package com.bookhub.repository;

import com.bookhub.entity.Prestamo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class PrestamoRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PrestamoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        // Crear la tabla 'prestamos' si no existe
        jdbcTemplate.execute(CREATE_TABLE_PRESTAMOS);
    }

    // SQL para crear la tabla de prestamos
    private static final String CREATE_TABLE_PRESTAMOS =
            "CREATE TABLE IF NOT EXISTS prestamos (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "fecha_prestamo DATE," +
                    "fecha_devolucion DATE," +
                    "estado BOOLEAN," +
                    "usuario_id INT," +
                    "libro_isbn VARCHAR(255)," +
                    "FOREIGN KEY (usuario_id) REFERENCES usuarios(id)," +
                    "FOREIGN KEY (libro_isbn) REFERENCES libros(isbn)" +
                    ");";

    // Registrar un nuevo préstamo
    private static final String INSERT_PRESTAMO =
            "INSERT INTO prestamos (fecha_prestamo, fecha_devolucion, estado, usuario_id, libro_isbn) VALUES (?, ?, ?, ?, ?)";

    // Marcar como devuelto un préstamo activo de un usuario y un libro específico
    private static final String UPDATE_DEVOLUCION =
            "UPDATE prestamos SET estado = FALSE WHERE libro_isbn = ? AND usuario_id = ? AND estado = TRUE";

    //  Consultar préstamos activos por usuario
    private static final String SELECT_ACTIVOS_BY_USER =
            "SELECT id, fecha_prestamo, fecha_devolucion, estado, usuario_id, libro_isbn FROM prestamos WHERE usuario_id = ? AND estado = TRUE";

    //  Historial de préstamos por usuario
    private static final String SELECT_HISTORIAL_BY_USER =
            "SELECT id, fecha_prestamo, fecha_devolucion, estado, usuario_id, libro_isbn FROM prestamos WHERE usuario_id = ?";

    // Implementación
    public void registrarPrestamo(Prestamo prestamo) {
        jdbcTemplate.update(INSERT_PRESTAMO,
                prestamo.getFechaPrestamo(),
                prestamo.getFechaDevolucion(),
                prestamo.getEstado(),
                prestamo.getUsuarioId(),
                prestamo.getLibroIsbn());
    }

    //  Implementación
    public int marcarComoDevuelto(String isbn, Integer usuarioId) {
        return jdbcTemplate.update(UPDATE_DEVOLUCION, isbn, usuarioId);
    }

    //  Implementación
    public List<Prestamo> findActivosByUsuario(Integer usuarioId) {
        return jdbcTemplate.query(SELECT_ACTIVOS_BY_USER,
                this::mapRowToPrestamo, // Usar RowMapper
                usuarioId);
    }

    //  Implementación
    public List<Prestamo> findHistorialByUsuario(Integer usuarioId) {
        return jdbcTemplate.query(SELECT_HISTORIAL_BY_USER,
                this::mapRowToPrestamo,
                usuarioId);
    }

    // Método para mapear el resultado de la BD al objeto Prestamo
    private Prestamo mapRowToPrestamo(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        Prestamo p = new Prestamo();
        p.setId(rs.getInt("id"));
        p.setFechaPrestamo(rs.getObject("fecha_prestamo", LocalDate.class));
        p.setFechaDevolucion(rs.getObject("fecha_devolucion", LocalDate.class));
        p.setEstado(rs.getBoolean("estado"));
        p.setUsuarioId(rs.getInt("usuario_id"));
        p.setLibroIsbn(rs.getString("libro_isbn"));
        return p;
    }
}
