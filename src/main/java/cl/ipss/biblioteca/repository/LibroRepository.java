package cl.ipss.biblioteca.repository;

import cl.ipss.biblioteca.entity.Libro;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * REPOSITORY: acceso a datos. Al extender JpaRepository, Spring genera
 * en tiempo de ejecución save, findById, findAll, deleteById, existsById...
 */
@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    /** Método derivado: Spring crea el SELECT leyendo el nombre del método. */
    boolean existsByIsbn(String isbn);

    /**
     * Búsqueda por texto en título, autor, ISBN o categoría (sin distinguir mayúsculas).
     * El texto nunca es null: con "" el LIKE '%%' devuelve todo el catálogo.
     */
    @Query("""
            select l from Libro l
            where (lower(l.titulo) like lower(concat('%', :texto, '%'))
                or lower(l.autor) like lower(concat('%', :texto, '%'))
                or lower(l.isbn) like lower(concat('%', :texto, '%'))
                or lower(l.categoria) like lower(concat('%', :texto, '%')))
              and (:soloDisponibles = false or l.ejemplaresDisponibles > 0)
            order by l.titulo
            """)
    List<Libro> buscar(@Param("texto") String texto, @Param("soloDisponibles") boolean soloDisponibles);

    /** Lee el libro bloqueando su fila (SELECT ... FOR UPDATE): dos préstamos simultáneos no pueden gastar la misma copia. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select l from Libro l where l.id = :id")
    Optional<Libro> buscarParaActualizar(@Param("id") Long id);
}
