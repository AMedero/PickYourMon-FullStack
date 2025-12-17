package ar.org.adriel_medero.java.pickyourmon.controller;

import ar.org.adriel_medero.java.pickyourmon.dto.UsuarioDTO;
import ar.org.adriel_medero.java.pickyourmon.model.Usuario;
import ar.org.adriel_medero.java.pickyourmon.model.enums.Rol;
import ar.org.adriel_medero.java.pickyourmon.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // -------------------------------- ENDPOINTS --------------------------------

    // REGISTRO (Crear Usuario)
    @PostMapping("/registro")
    public ResponseEntity<UsuarioDTO> registrar(@RequestBody Usuario usuario) {
        // Regla de negocio: Si no trae rol, le asignamos USER por defecto
        if (usuario.getRol() == null) {
            usuario.setRol(Rol.USER); 
        }

        // Guardamos en BD
        usuarioService.guardar(usuario);
        
        // Devolvemos 201 Created y el usuario (convertido a DTO para ocultar password)
        return ResponseEntity.status(HttpStatus.CREATED).body(convertirADTO(usuario));
    }

    // LISTAR (Para verificar que se guardaron)
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listar() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        List<UsuarioDTO> dtos = usuarios.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // -------------------------------- MAPPER MANUAL --------------------------------
    
    // Convertimos de Entidad (con password) a DTO (sin password)
    private UsuarioDTO convertirADTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol());
        dto.setDireccion(usuario.getDireccion());
        dto.setDni(usuario.getDni());
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        // IMPORTANTE: No setear la contraseña acá. Seguridad básica.
        return dto;
    }
}
