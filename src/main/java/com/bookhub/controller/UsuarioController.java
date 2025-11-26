package com.bookhub.controller;

import com.bookhub.dto.UsuarioRequest;
import com.bookhub.dto.UsuarioResponse;
import com.bookhub.entity.Usuario;
import com.bookhub.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Gestion de usuarios del sistema bibliotecario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // --- Crear usuario ---
    @Operation(summary = "Registrar un nuevo usuario", description = "Crea un usuario validando email unico y telefono valido.")
    @PostMapping
    public ResponseEntity<?> registrarUsuario(@RequestBody UsuarioRequest request) {
        try {
            Usuario nuevo = new Usuario();
            nuevo.setCedula(request.getCedula());
            nuevo.setNombre(request.getNombre());
            nuevo.setEmail(request.getEmail());
            nuevo.setTelefono(request.getTelefono());

            int idGenerado = usuarioService.registrarUsuario(nuevo);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Usuario registrado correctamente");
            respuesta.put("usuarioId", idGenerado);
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (IllegalArgumentException | IllegalStateException e) {
            String mensaje = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
            if (mensaje.contains("email") || mensaje.contains("duplic") || mensaje.contains("ya esta registrado")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // --- Listar todos ---
    @Operation(summary = "Listar todos los usuarios", description = "Devuelve todos los usuarios registrados.")
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        List<UsuarioResponse> usuarios = usuarioService.listarUsuarios()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuarios);
    }

    // --- Buscar por ID ---
    @Operation(summary = "Obtener un usuario por su ID", description = "Busca un usuario existente.")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUsuario(@PathVariable int id) {
        try {
            Usuario u = usuarioService.obtenerUsuarioPorId(id);
            return ResponseEntity.ok(toResponse(u));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // --- Buscar por cedula ---
    @Operation(summary = "Obtener un usuario por su cedula", description = "Busca un usuario existente por cedula.")
    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<?> obtenerUsuarioPorCedula(@PathVariable String cedula) {
        try {
            Usuario u = usuarioService.obtenerUsuarioPorCedula(cedula);
            return ResponseEntity.ok(toResponse(u));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // --- Contar prestamos activos ---
    @Operation(summary = "Contar prestamos activos", description = "Devuelve la cantidad de prestamos activos de un usuario.")
    @GetMapping("/{id}/prestamos/activos")
    public ResponseEntity<Map<String, Integer>> contarPrestamosActivos(@PathVariable int id) {
        Map<String, Integer> respuesta = new HashMap<>();
        respuesta.put("prestamosActivos", usuarioService.contarPrestamosActivos(id));
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Contar prestamos activos por cedula", description = "Devuelve la cantidad de prestamos activos de un usuario.")
    @GetMapping("/cedula/{cedula}/prestamos/activos")
    public ResponseEntity<Map<String, Integer>> contarPrestamosActivosPorCedula(@PathVariable String cedula) {
        Usuario usuario = usuarioService.obtenerUsuarioPorCedula(cedula);
        Map<String, Integer> respuesta = new HashMap<>();
        respuesta.put("prestamosActivos", usuarioService.contarPrestamosActivos(usuario.getId()));
        return ResponseEntity.ok(respuesta);
    }

    // --- Validar si puede prestar ---
    @Operation(summary = "Validar si puede solicitar mas prestamos", description = "Verifica la regla RN001 (maximo 3 prestamos activos).")
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

    @Operation(summary = "Validar si puede solicitar mas prestamos (cedula)", description = "Verifica RN001 usando cedula.")
    @GetMapping("/cedula/{cedula}/puede-prestar")
    public ResponseEntity<Map<String, Object>> puedePrestarPorCedula(@PathVariable String cedula) {
        try {
            Usuario usuario = usuarioService.obtenerUsuarioPorCedula(cedula);
            boolean puede = usuarioService.puedePrestar(usuario.getId());
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("usuarioCedula", cedula);
            respuesta.put("puedePrestar", puede);
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // --- Eliminar usuario si no tiene prestamos activos ---
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario si no tiene prestamos activos (Regla RN005).")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable int id) {
        try {
            usuarioService.validarUsuarioSinPrestamosActivos(id);
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Usuario eliminado correctamente (simulado, sin logica real de borrado)");
            return ResponseEntity.ok(respuesta);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Actualizar usuario", description = "Permite editar los datos de un usuario existente.")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable int id, @RequestBody UsuarioRequest request) {
        try {
            Usuario actualizado = new Usuario();
            actualizado.setId(id);
            actualizado.setCedula(request.getCedula());
            actualizado.setNombre(request.getNombre());
            actualizado.setEmail(request.getEmail());
            actualizado.setTelefono(request.getTelefono());

            int result = usuarioService.actualizarUsuario(actualizado);
            Map<String, Object> resp = new HashMap<>();
            resp.put("mensaje", "Usuario actualizado correctamente");
            resp.put("usuarioId", id);
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getCedula(), usuario.getNombre(), usuario.getEmail(), usuario.getTelefono());
    }
}