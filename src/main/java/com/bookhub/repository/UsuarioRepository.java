package com.bookhub.repository;

import com.bookhub.entity.Usuario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRepository {

    private static final String SQL_INSERT_USUARIO =
        "INSERT INTO usuarios (id, nombre, email, telefono) VALUES (?, ?, ?, ?)";
    private static final String SQL_SELECT_USUARIO_POR_ID =
        "SELECT id, nombre, email, telefono FROM usuarios WHERE id = ?";
    private static final String SQL_SELECT_TODOS =
        "SELECT id, nombre, email, telefono FROM usuarios ORDER BY nombre";
    private static final String SQL_COUNT_PRESTAMOS_ACTIVOS =
        "SELECT COUNT(*) FROM prestamos p " +
        "INNER JOIN usuarios u ON u.id = p.usuario_id " +
        "WHERE p.estado = TRUE AND p.usuario_id = ?";

    private static final RowMapper<Usuario> USUARIO_ROW_MAPPER = UsuarioRepository::mapRow;

    private final JdbcTemplate jdbcTemplate;

    public UsuarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int registrar(Usuario usuario) {
        return jdbcTemplate.update(
            SQL_INSERT_USUARIO,
            usuario.getId(),
            usuario.getNombre(),
            usuario.getEmail(),
            usuario.getTelefono()
        );
    }

    public Optional<Usuario> findById(int id) {
        List<Usuario> resultados = jdbcTemplate.query(SQL_SELECT_USUARIO_POR_ID, USUARIO_ROW_MAPPER, id);
        return resultados.stream().findFirst();
    }

    public Usuario consultarPorId(int id) {
        return findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No existe usuario con id " + id));
    }

    public List<Usuario> listarTodos() {
        return jdbcTemplate.query(SQL_SELECT_TODOS, USUARIO_ROW_MAPPER);
    }

    public int contarPrestamosActivos(int usuarioId) {
        Integer cantidad = jdbcTemplate.queryForObject(SQL_COUNT_PRESTAMOS_ACTIVOS, Integer.class, usuarioId);
        return cantidad != null ? cantidad : 0;
    }

    public boolean puedeRegistrarPrestamo(int usuarioId) {
        int prestamosActivos = contarPrestamosActivos(usuarioId);
        return findById(usuarioId)
            .map(usuario -> usuario.puedePrestar(prestamosActivos))
            .orElse(false);
    }

    private static Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Usuario(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("email"),
            rs.getString("telefono")
        );
    }
}
