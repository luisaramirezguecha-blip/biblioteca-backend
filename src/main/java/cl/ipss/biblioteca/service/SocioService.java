package cl.ipss.biblioteca.service;

import cl.ipss.biblioteca.dto.CrearSocioRequest;
import cl.ipss.biblioteca.dto.SocioResponse;

import java.util.List;

public interface SocioService {

    List<SocioResponse> listar();

    SocioResponse obtener(Long id);

    SocioResponse crear(CrearSocioRequest request);
}
