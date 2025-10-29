package com.bookhub.service;

import com.bookhub.entity.Usuario;
import com.bookhub.repository.UsuarioRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Registrar usuario con validaciones completas (email único + teléfono + id duplicado)
    public int registrarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Debe proporcionar la información del usuario");
        }

        // Validar ID duplicado
        if (usuarioRepository.findById(usuario.getId()).isPresent()) {
            throw new IllegalStateException("Ya existe un usuario con id " + usuario.getId());
        }

        // Validar que el email no esté duplicado
        usuarioRepository.findByEmail(usuario.getEmail())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("El email ya está registrado: " + usuario.getEmail());
                });

        // Validar teléfono
        if (usuario.getTelefono() != null && !usuario.getTelefono().matches("^\\d{7,15}$")) {
            throw new IllegalArgumentException("El número de teléfono no es válido. Debe tener entre 7 y 15 dígitos.");
        }

        // Si todo está bien, registrar
        return usuarioRepository.registrar(usuario);
    }

    // Consultar usuario por ID
    public Usuario obtenerUsuarioPorId(int id) {
        return usuarioRepository.consultarPorId(id);
    }

    // Listar todos los usuarios
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.listarTodos();
    }

    // Contar préstamos activos
    public int contarPrestamosActivos(int usuarioId) {
        return usuarioRepository.contarPrestamosActivos(usuarioId);
    }

    // Validar si un usuario puede prestar (por id)
    public boolean puedePrestar(int usuarioId) {
        obtenerUsuarioPorId(usuarioId); // valida existencia
        return usuarioRepository.puedeRegistrarPrestamo(usuarioId);
    }

    // Validar si un usuario puede prestar (por número de préstamos)
    public boolean puedePrestar(int usuarioId, int prestamosActivos) {
        Usuario usuario = obtenerUsuarioPorId(usuarioId);
        return usuario.puedePrestar(prestamosActivos);
    }

    // Regla RN001: máximo de préstamos activos
    public void validarUsuarioPuedePrestar(int usuarioId) {
        if (!puedePrestar(usuarioId)) {
            throw new IllegalStateException("RN001: El usuario ya tiene el máximo permitido de préstamos activos");
        }
    }

    // Regla RN005: no puede eliminarse si tiene préstamos activos
    public void validarUsuarioSinPrestamosActivos(int usuarioId) {
        if (contarPrestamosActivos(usuarioId) > 0) {
            throw new IllegalStateException("RN005: El usuario posee préstamos activos");
        }
    }
}
