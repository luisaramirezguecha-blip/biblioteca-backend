package cl.ipss.biblioteca.service;

import cl.ipss.biblioteca.dto.CrearPrestamoRequest;
import cl.ipss.biblioteca.dto.PrestamoResponse;
import cl.ipss.biblioteca.enums.EstadoPrestamo;

import java.util.List;

public interface PrestamoService {

    /** Los filtros son opcionales (null = sin filtrar). Los más recientes primero. */
    List<PrestamoResponse> listar(Long socioId, EstadoPrestamo estado);

    PrestamoResponse obtener(Long id);

    /** Presta un libro a un socio aplicando las reglas de la biblioteca. */
    PrestamoResponse prestar(CrearPrestamoRequest request);

    /** Registra la devolución y libera la copia. */
    PrestamoResponse devolver(Long id);
}
