package cl.ipss.biblioteca.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** DTO de entrada para POST /api/libros. Todas las copias nacen disponibles. */
public record CrearLibroRequest(
        @NotBlank(message = "El título es obligatorio")
        @Size(max = 150, message = "El título no puede superar los 150 caracteres")
        String titulo,

        @NotBlank(message = "El autor es obligatorio")
        @Size(max = 100, message = "El autor no puede superar los 100 caracteres")
        String autor,

        @NotBlank(message = "El ISBN es obligatorio")
        @Pattern(regexp = "^[0-9Xx-]{10,17}$", message = "El ISBN debe tener entre 10 y 17 dígitos o guiones")
        String isbn,

        @NotBlank(message = "La categoría es obligatoria")
        @Size(max = 50, message = "La categoría no puede superar los 50 caracteres")
        String categoria,

        @NotNull(message = "El año de publicación es obligatorio")
        @Min(value = 1000, message = "El año de publicación debe ser mayor o igual a 1000")
        @Max(value = 2100, message = "El año de publicación debe ser menor o igual a 2100")
        Integer anioPublicacion,

        @NotNull(message = "La cantidad de ejemplares es obligatoria")
        @Min(value = 1, message = "Debe haber al menos 1 ejemplar")
        @Max(value = 100, message = "No puede haber más de 100 ejemplares")
        Integer ejemplares
) {
}
