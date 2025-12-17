package ar.org.adriel_medero.java.pickyourmon.controller;

import ar.org.adriel_medero.java.pickyourmon.dto.ProductoDTO;
import ar.org.adriel_medero.java.pickyourmon.model.Producto;
import ar.org.adriel_medero.java.pickyourmon.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // -------------------------------- ENDPOINTS --------------------------------

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listar() {
        // buscamos todos los productos (entidades)
        List<Producto> productos = productoService.listarProductos();
        
        // las convertimos a DTOs con el mapper manual
        List<ProductoDTO> dtos = productos.stream()
                .map(this::convertirADTO) // convierte cada Producto a ProductoDTO
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .map(this::convertirADTO) // Si lo encuentra, lo convierte
                .map(ResponseEntity::ok)  // Y lo devuelve con OK
                .orElse(ResponseEntity.notFound().build());
    }

    // -------------------------------- MAPPER MANUAL (TRADUCTOR) --------------------------------
    // convierte entidad (DB) a DTO (JSON limpio)
    private ProductoDTO convertirADTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setImagenUrl(producto.getImagenUrl());
        
        // mandamos solo el nombre de la categoría y no todo el objeto
        if (producto.getCategoria() != null) {
            dto.setNombreCategoria(producto.getCategoria().getNombre());
        }
        return dto;
    }
}