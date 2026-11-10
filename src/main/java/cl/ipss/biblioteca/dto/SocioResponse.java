package cl.ipss.biblioteca.dto;

import java.time.LocalDateTime;

/** DTO de salida de un socio. */
public record SocioResponse(
        Long id,
        String nombre,
        String email,
        LocalDateTime creadoEn
) {
}
