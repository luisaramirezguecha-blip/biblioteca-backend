package cl.ipss.biblioteca.mapper;

import cl.ipss.biblioteca.dto.CrearLibroRequest;
import cl.ipss.biblioteca.dto.DisponibilidadResponse;
import cl.ipss.biblioteca.dto.LibroResponse;
import cl.ipss.biblioteca.entity.Libro;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/** Patrón Mapper: convierte entre DTOs (lo que viaja por HTTP) y la Entity (lo que se guarda). */
@Component
public class LibroMapper {

    /** Un libro nuevo nace con todas sus copias disponibles. */
    public Libro aEntidad(CrearLibroRequest r) {
        Libro entidad = new Libro();
        entidad.setTitulo(r.titulo().trim());
        entidad.setAutor(r.autor().trim());
        entidad.setIsbn(r.isbn().trim());
        entidad.setCategoria(r.categoria().trim());
        entidad.setAnioPublicacion(r.anioPublicacion());
        entidad.setEjemplaresTotales(r.ejemplares());
        entidad.setEjemplaresDisponibles(r.ejemplares());
        return entidad;
    }

    public LibroResponse aRespuesta(Libro l) {
        return new LibroResponse(l.getId(), l.getTitulo(), l.getAutor(), l.getIsbn(), l.getCategoria(),
                l.getAnioPublicacion(), l.getEjemplaresTotales(), l.getEjemplaresDisponibles(),
                l.getEjemplaresDisponibles() > 0, l.getCreadoEn());
    }

    public DisponibilidadResponse aDisponibilidad(Libro l, LocalDate proximaDevolucion) {
        return new DisponibilidadResponse(l.getId(), l.getTitulo(), l.getEjemplaresTotales(),
                l.getEjemplaresDisponibles(), l.getEjemplaresDisponibles() > 0, proximaDevolucion);
    }
}
