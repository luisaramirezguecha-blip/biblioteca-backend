package cl.ipss.biblioteca.service;

import cl.ipss.biblioteca.dto.CrearLibroRequest;
import cl.ipss.biblioteca.dto.DisponibilidadResponse;
import cl.ipss.biblioteca.dto.LibroResponse;

import java.util.List;

/**
 * SERVICE (interfaz): define QUÉ operaciones de negocio existen sobre el catálogo.
 * El controller depende de esta interfaz; la lógica está en LibroServiceImpl.
 */
public interface LibroService {

    /** Busca por título, autor, ISBN o categoría. Sin texto devuelve todo el catálogo. */
    List<LibroResponse> buscar(String texto, boolean soloDisponibles);

    LibroResponse obtener(Long id);

    /** Cuántas copias hay y cuándo volverá la próxima si no queda ninguna. */
    DisponibilidadResponse consultarDisponibilidad(Long id);

    LibroResponse crear(CrearLibroRequest request);
}
