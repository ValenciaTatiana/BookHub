package com.bookhub.service;

import com.bookhub.entity.Usuario;
import com.bookhub.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Registrar usuario
    public int registrarUsuario(Usuario usuario) {

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
