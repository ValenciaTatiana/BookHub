package com.bookhub.service;

import com.bookhub.entity.Usuario;
import com.bookhub.repository.UsuarioRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario registrarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Debe proporcionar la informacion del usuario");
        }
        if (usuarioRepository.findById(usuario.getId()).isPresent()) {
            throw new IllegalStateException("Ya existe un usuario con id " + usuario.getId());
        }
        usuarioRepository.registrar(usuario);
        return usuario;
    }

    public Usuario obtenerUsuarioPorId(int id) {
        return usuarioRepository.consultarPorId(id);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.listarTodos();
    }

    public int contarPrestamosActivos(int usuarioId) {
        return usuarioRepository.contarPrestamosActivos(usuarioId);
    }

    public boolean puedePrestar(int usuarioId) {
        obtenerUsuarioPorId(usuarioId); // Valida existencia
        return usuarioRepository.puedeRegistrarPrestamo(usuarioId);
    }

    public boolean puedePrestar(int usuarioId, int prestamosActivos) {
        Usuario usuario = obtenerUsuarioPorId(usuarioId);
        return usuario.puedePrestar(prestamosActivos);
    }

    public void validarUsuarioPuedePrestar(int usuarioId) {
        if (!puedePrestar(usuarioId)) {
            throw new IllegalStateException("RN001: El usuario ya tiene el maximo permitido de prestamos activos");
        }
    }

    public void validarUsuarioSinPrestamosActivos(int usuarioId) {
        if (contarPrestamosActivos(usuarioId) > 0) {
            throw new IllegalStateException("RN005: El usuario posee prestamos activos");
        }
    }
}
