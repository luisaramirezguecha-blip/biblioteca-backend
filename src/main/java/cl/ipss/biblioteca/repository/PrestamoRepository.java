package cl.ipss.biblioteca.repository;

import cl.ipss.biblioteca.entity.Prestamo;
import cl.ipss.biblioteca.enums.EstadoPrestamo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    /** Cuántos préstamos tiene un socio en cierto estado (para el límite de préstamos activos). */
    long countBySocioIdAndEstado(Long socioId, EstadoPrestamo estado);

    boolean existsBySocioIdAndLibroIdAndEstado(Long socioId, Long libroId, EstadoPrestamo estado);

    /** El préstamo activo del libro que vence primero: dice cuándo volverá una copia. */
    Optional<Prestamo> findFirstByLibroIdAndEstadoOrderByFechaVencimientoAsc(Long libroId, EstadoPrestamo estado);

    // @EntityGraph trae libro y socio en la misma consulta (evita una consulta extra por cada fila)
    @EntityGraph(attributePaths = {"libro", "socio"})
    List<Prestamo> findAllByOrderByIdDesc();

    @EntityGraph(attributePaths = {"libro", "socio"})
    List<Prestamo> findBySocioIdOrderByIdDesc(Long socioId);

    @EntityGraph(attributePaths = {"libro", "socio"})
    Optional<Prestamo> findConDetalleById(Long id);
}
