package cl.ipss.biblioteca.dto;

import java.time.LocalDateTime;

/** DTO de salida: lo que viaja en el JSON hacia Angular (la "View" de MVC en una API REST). */
public record LibroResponse(
        Long id,
        String titulo,
        String autor,
        String isbn,
        String categoria,
        Integer anioPublicacion,
        Integer ejemplaresTotales,
        Integer ejemplaresDisponibles,
        boolean disponible,
        LocalDateTime creadoEn
) {
}
