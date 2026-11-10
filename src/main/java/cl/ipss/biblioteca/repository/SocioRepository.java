package cl.ipss.biblioteca.repository;

import cl.ipss.biblioteca.entity.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocioRepository extends JpaRepository<Socio, Long> {

    boolean existsByEmail(String email);
}
