package cl.ipss.biblioteca.mapper;

import cl.ipss.biblioteca.dto.PrestamoResponse;
import cl.ipss.biblioteca.entity.Prestamo;
import cl.ipss.biblioteca.enums.EstadoPrestamo;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PrestamoMapper {

    /** Debe llamarse dentro de la transacción del service: lee el libro y el socio (relaciones LAZY). */
    public PrestamoResponse aRespuesta(Prestamo p) {
        boolean vencido = p.getEstado() == EstadoPrestamo.ACTIVO
                && p.getFechaVencimiento().isBefore(LocalDate.now());
        return new PrestamoResponse(p.getId(), p.getLibro().getId(), p.getLibro().getTitulo(),
                p.getSocio().getId(), p.getSocio().getNombre(), p.getFechaPrestamo(),
                p.getFechaVencimiento(), p.getFechaDevolucion(), p.getEstado(), vencido);
    }
}
