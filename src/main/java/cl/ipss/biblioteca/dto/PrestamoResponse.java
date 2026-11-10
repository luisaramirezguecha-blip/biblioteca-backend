package cl.ipss.biblioteca.dto;

import cl.ipss.biblioteca.enums.EstadoPrestamo;

import java.time.LocalDate;

/** DTO de salida de un préstamo. "vencido" es un dato calculado: ACTIVO y con la fecha de vencimiento ya pasada. */
public record PrestamoResponse(
        Long id,
        Long libroId,
        String tituloLibro,
        Long socioId,
        String nombreSocio,
        LocalDate fechaPrestamo,
        LocalDate fechaVencimiento,
        LocalDate fechaDevolucion,
        EstadoPrestamo estado,
        boolean vencido
) {
}
