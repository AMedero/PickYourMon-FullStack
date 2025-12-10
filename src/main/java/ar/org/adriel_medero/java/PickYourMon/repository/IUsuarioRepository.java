package ar.org.adriel_medero.java.pickyourmon.repository;

import ar.org.adriel_medero.java.pickyourmon.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email); // para hacer login
    boolean existsByEmail(String email);         // para hacer registro
}