package cl.ipss.biblioteca.mapper;

import cl.ipss.biblioteca.dto.CrearSocioRequest;
import cl.ipss.biblioteca.dto.SocioResponse;
import cl.ipss.biblioteca.entity.Socio;
import org.springframework.stereotype.Component;

@Component
public class SocioMapper {

    public Socio aEntidad(CrearSocioRequest r) {
        Socio entidad = new Socio();
        entidad.setNombre(r.nombre().trim());
        entidad.setEmail(r.email().trim().toLowerCase());
        return entidad;
    }

    public SocioResponse aRespuesta(Socio s) {
        return new SocioResponse(s.getId(), s.getNombre(), s.getEmail(), s.getCreadoEn());
    }
}
