package com.bookhub.repository;

import com.bookhub.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UsuarioRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Registrar usuario
    public int registrar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (id, nombre, email, telefono) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, usuario.getId(), usuario.getNombre(), usuario.getEmail(), usuario.getTelefono());
    }

    // Consultar un usuario por ID
    public Usuario consultarPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, this::mapRowToUsuario);
    }

    // Listar todos los usuarios
    public List<Usuario> listarTodos() {
        String sql = "SELECT * FROM usuarios";
        return jdbcTemplate.query(sql, this::mapRowToUsuario);
    }

    // Función para mapear resultados de la base de datos a objetos Usuario
    private Usuario mapRowToUsuario(ResultSet rs, int rowNum) throws SQLException {
        return new Usuario(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("email"),
                rs.getString("telefono")
        );
    }
}
