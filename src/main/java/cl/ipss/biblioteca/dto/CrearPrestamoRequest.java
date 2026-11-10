package cl.ipss.biblioteca.dto;

import jakarta.validation.constraints.NotNull;

/** DTO de entrada para POST /api/prestamos: quién se lleva qué libro. Las fechas las calcula el sistema. */
public record CrearPrestamoRequest(
        @NotNull(message = "El libro es obligatorio")
        Long libroId,

        @NotNull(message = "El socio es obligatorio")
        Long socioId
) {
}
