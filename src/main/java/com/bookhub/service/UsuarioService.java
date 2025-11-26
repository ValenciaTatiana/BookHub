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

    // Registrar usuario con validaciones completas (email unico + telefono + cedula)
    public int registrarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Debe proporcionar la informacion del usuario");
        }

        validarCedula(usuario.getCedula(), usuario.getId());

        // Validar que el email no este duplicado
        usuarioRepository.findByEmail(usuario.getEmail())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("El email ya esta registrado: " + usuario.getEmail());
                });

        // Validar telefono
        if (usuario.getTelefono() != null && !usuario.getTelefono().matches("^\\d{7,15}$")) {
            throw new IllegalArgumentException("El numero de telefono no es valido. Debe tener entre 7 y 15 digitos.");
        }

        // Si todo esta bien, registrar
        return usuarioRepository.registrar(usuario);
    }

    // Consultar usuario por ID
    public Usuario obtenerUsuarioPorId(int id) {
        return usuarioRepository.consultarPorId(id);
    }

    // Consultar usuario por cedula
    public Usuario obtenerUsuarioPorCedula(String cedula) {
        return usuarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new IllegalArgumentException("No existe usuario con cedula " + cedula));
    }

    // Listar todos los usuarios
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.listarTodos();
    }

    // Contar prestamos activos
    public int contarPrestamosActivos(int usuarioId) {
        return usuarioRepository.contarPrestamosActivos(usuarioId);
    }

    // Validar si un usuario puede prestar (por id)
    public boolean puedePrestar(int usuarioId) {
        obtenerUsuarioPorId(usuarioId); // valida existencia
        return usuarioRepository.puedeRegistrarPrestamo(usuarioId);
    }

    // Validar si un usuario puede prestar (por numero de prestamos)
    public boolean puedePrestar(int usuarioId, int prestamosActivos) {
        Usuario usuario = obtenerUsuarioPorId(usuarioId);
        return usuario.puedePrestar(prestamosActivos);
    }

    // Regla RN001: maximo de prestamos activos
    public void validarUsuarioPuedePrestar(int usuarioId) {
        if (!puedePrestar(usuarioId)) {
            throw new IllegalStateException("RN001: El usuario ya tiene el maximo permitido de prestamos activos");
        }
    }

    // Regla RN005: no puede eliminarse si tiene prestamos activos
    public void validarUsuarioSinPrestamosActivos(int usuarioId) {
        if (contarPrestamosActivos(usuarioId) > 0) {
            throw new IllegalStateException("RN005: El usuario posee prestamos activos");
        }
    }

    public int actualizarUsuario(Usuario usuario) {
        usuarioRepository.findById(usuario.getId())
                .orElseThrow(() -> new IllegalArgumentException("No existe usuario con ID " + usuario.getId()));

        validarCedula(usuario.getCedula(), usuario.getId());

        // Validar email duplicado
        usuarioRepository.findByEmail(usuario.getEmail())
                .filter(u -> u.getId() != usuario.getId())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("El email ya esta registrado: " + usuario.getEmail());
                });

        if (usuario.getTelefono() != null && !usuario.getTelefono().matches("^\\d{7,15}$")) {
            throw new IllegalArgumentException("El numero de telefono no es valido. Debe tener entre 7 y 15 digitos.");
        }
        return usuarioRepository.actualizar(usuario);
    }

    private void validarCedula(String cedula, int usuarioId) {
        if (cedula == null || cedula.isBlank()) {
            throw new IllegalArgumentException("La cedula es obligatoria");
        }
        if (!cedula.matches("^\\d{6,12}$")) {
            throw new IllegalArgumentException("La cedula debe tener entre 6 y 12 digitos numericos");
        }

        usuarioRepository.findByCedula(cedula)
                .filter(u -> u.getId() != usuarioId)
                .ifPresent(u -> {
                    throw new IllegalArgumentException("La cedula ya esta registrada: " + cedula);
                });
    }
}
