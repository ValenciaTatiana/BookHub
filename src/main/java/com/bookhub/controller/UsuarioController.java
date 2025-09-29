package com.bookhub.controller;

import com.bookhub.entity.Usuario;
import com.bookhub.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Registrar usuario
    @PostMapping
    public String registrarUsuario(@RequestBody Usuario usuario) {
        usuarioService.registrarUsuario(usuario);
        return "Usuario registrado exitosamente";
    }

    // Consultar usuario por ID
    @GetMapping("/{id}")
    public Usuario obtenerUsuario(@PathVariable int id) {
        return usuarioService.obtenerUsuarioPorId(id);
    }

    // Listar todos los usuarios
    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    // Validar si puede prestar (ejemplo: pasando cantidad de préstamos activos)
    @GetMapping("/{id}/puede-prestar/{prestamosActivos}")
    public boolean puedePrestar(@PathVariable int id, @PathVariable int prestamosActivos) {
        return usuarioService.puedePrestar(id, prestamosActivos);
    }
}
