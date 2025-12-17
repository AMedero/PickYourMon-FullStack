package ar.org.adriel_medero.java.pickyourmon.controller;

import ar.org.adriel_medero.java.pickyourmon.dto.CompraDTO;
import ar.org.adriel_medero.java.pickyourmon.dto.DetalleCompraDTO;
import ar.org.adriel_medero.java.pickyourmon.model.DetalleOrden;
import ar.org.adriel_medero.java.pickyourmon.model.Orden;
import ar.org.adriel_medero.java.pickyourmon.model.Producto;
import ar.org.adriel_medero.java.pickyourmon.model.Usuario;
import ar.org.adriel_medero.java.pickyourmon.service.OrdenService;
import ar.org.adriel_medero.java.pickyourmon.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    @Autowired
    private OrdenService ordenService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/comprar")
    public ResponseEntity<?> crearOrden(@RequestBody CompraDTO compraDTO) {
        try {
            // 1. Buscamos al Usuario
            Usuario usuario = usuarioService.buscarPorId(compraDTO.getUsuarioId())
                    .orElse(null);

            if (usuario == null) {
                return ResponseEntity.badRequest().body("Usuario no encontrado");
            }

            // 2. Preparamos la lista de DetalleOrden para enviarla al Service
            List<DetalleOrden> detalles = new ArrayList<>();

            for (DetalleCompraDTO item : compraDTO.getItems()) {
                // Solo necesitamos el ID del producto y la cantidad
                // El service se encarga de buscar el precio real y descontar stock
                Producto productoMock = new Producto();
                productoMock.setId(item.getProductoId());

                DetalleOrden detalle = new DetalleOrden();
                detalle.setProducto(productoMock);
                detalle.setCantidad(item.getCantidad());
                
                detalles.add(detalle);
            }

            // 3. Llamamos al Service que hace toda la magia (Transaction, Stock, Total)
            Orden ordenCreada = ordenService.crearOrden(usuario, detalles);

            // 4. Respondemos éxito
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Orden creada con éxito. ID: " + ordenCreada.getId() + " - Total: $" + ordenCreada.getTotal());

        } catch (RuntimeException e) {
            // Si falla por falta de stock o producto no encontrado (errores del Service)
            return ResponseEntity.badRequest().body("Error al procesar la compra: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error inesperado: " + e.getMessage());
        }
    }
}