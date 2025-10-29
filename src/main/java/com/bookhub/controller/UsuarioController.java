package com.bookhub.controller;

import com.bookhub.entity.Usuario;
import com.bookhub.service.UsuarioService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import com.bookhub.dto.UsuarioRequest;
import com.bookhub.dto.UsuarioResponse;
import java.util.stream.Collectors;




@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema bibliotecario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // --- Crear usuario ---
    @Operation(summary = "Registrar un nuevo usuario", description = "Crea un usuario validando email único y teléfono válido.")
    @PostMapping
    public ResponseEntity<?> registrarUsuario(@RequestBody UsuarioRequest request) {
        try {
            Usuario nuevo = new Usuario();
            nuevo.setNombre(request.getNombre());
            nuevo.setEmail(request.getEmail());
            nuevo.setTelefono(request.getTelefono());

            int idGenerado = usuarioService.registrarUsuario(nuevo);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Usuario registrado correctamente");
            respuesta.put("usuarioId", idGenerado);
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    // --- Listar todos ---
    @Operation(summary = "Listar todos los usuarios", description = "Devuelve todos los usuarios registrados.")
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        List<UsuarioResponse> usuarios = usuarioService.listarUsuarios()
                .stream()
                .map(u -> new UsuarioResponse(u.getId(), u.getNombre(), u.getEmail(), u.getTelefono()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuarios);
    }

    // --- Buscar por ID ---
    @Operation(summary = "Obtener un usuario por su ID", description = "Busca un usuario existente.")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUsuario(@PathVariable int id) {
        try {
            Usuario u = usuarioService.obtenerUsuarioPorId(id);
            UsuarioResponse respuesta = new UsuarioResponse(u.getId(), u.getNombre(), u.getEmail(), u.getTelefono());
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // --- Contar préstamos activos ---
    @Operation(summary = "Contar préstamos activos", description = "Devuelve la cantidad de préstamos activos de un usuario.")
    @GetMapping("/{id}/prestamos/activos")
    public ResponseEntity<Map<String, Integer>> contarPrestamosActivos(@PathVariable int id) {
        Map<String, Integer> respuesta = new HashMap<>();
        respuesta.put("prestamosActivos", usuarioService.contarPrestamosActivos(id));
        return ResponseEntity.ok(respuesta);
    }

    // --- Validar si puede prestar ---
    @Operation(summary = "Validar si puede solicitar más préstamos", description = "Verifica la regla RN001 (máximo 3 préstamos activos).")
    @GetMapping("/{id}/puede-prestar")
    public ResponseEntity<Map<String, Object>> puedePrestar(@PathVariable int id) {
        try {
            boolean puede = usuarioService.puedePrestar(id);
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("usuarioId", id);
            respuesta.put("puedePrestar", puede);
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // --- Eliminar usuario si no tiene préstamos activos ---
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario si no tiene préstamos activos (Regla RN005).")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable int id) {
        try {
            usuarioService.validarUsuarioSinPrestamosActivos(id);
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Usuario eliminado correctamente (simulado, sin lógica real de borrado)");
            return ResponseEntity.ok(respuesta);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
}