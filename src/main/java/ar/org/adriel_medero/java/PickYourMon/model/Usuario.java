package ar.org.adriel_medero.java.pickyourmon.model;

import ar.org.adriel_medero.java.pickyourmon.model.enums.Rol;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String nombre;
    private String apellido;
    private String dni;
    
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    private String direccion;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol")
    private Rol rol = Rol.USER; // por defecto es usuario comun
}