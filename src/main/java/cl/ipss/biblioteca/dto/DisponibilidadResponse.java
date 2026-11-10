package cl.ipss.biblioteca.dto;

import java.time.LocalDate;

/**
 * DTO de salida para GET /api/libros/{id}/disponibilidad.
 * Si no quedan copias, proximaDevolucion dice cuándo vence el préstamo que antes regresará (puede ser una fecha pasada si está atrasado).
 */
public record DisponibilidadResponse(
        Long libroId,
        String titulo,
        Integer ejemplaresTotales,
        Integer ejemplaresDisponibles,
        boolean disponible,
        LocalDate proximaDevolucion
) {
}
