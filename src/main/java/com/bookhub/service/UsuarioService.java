package com.bookhub.service;

import com.bookhub.entity.Usuario;
import com.bookhub.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Registrar usuario con validación de email único
    public int registrarUsuario(Usuario usuario) {
        // Validar que el email no esté duplicado
        usuarioRepository.findByEmail(usuario.getEmail())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("El email ya está registrado: " + usuario.getEmail());
                });

        // Validar que el teléfono no esté vacío o incorrecto
        if (usuario.getTelefono() != null && !usuario.getTelefono().matches("^\\d{7,15}$")) {
            throw new IllegalArgumentException("El número de teléfono no es válido. Debe tener entre 7 y 15 dígitos.");
        }

        // Si pasa las validaciones, registrar
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

    // Validar si un usuario puede prestar más libros
    public boolean puedePrestar(int id, int prestamosActivos) {
        Usuario usuario = usuarioRepository.consultarPorId(id);
        return usuario.puedePrestar(prestamosActivos);
    }
}
