package cl.ipss.biblioteca.service.impl;

import cl.ipss.biblioteca.dto.CrearSocioRequest;
import cl.ipss.biblioteca.dto.SocioResponse;
import cl.ipss.biblioteca.entity.Socio;
import cl.ipss.biblioteca.exception.DatoDuplicadoException;
import cl.ipss.biblioteca.exception.RecursoNoEncontradoException;
import cl.ipss.biblioteca.mapper.SocioMapper;
import cl.ipss.biblioteca.repository.SocioRepository;
import cl.ipss.biblioteca.service.SocioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SocioServiceImpl implements SocioService {

    private static final Logger log = LoggerFactory.getLogger(SocioServiceImpl.class);

    private final SocioRepository repository;
    private final SocioMapper mapper;

    public SocioServiceImpl(SocioRepository repository, SocioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SocioResponse> listar() {
        log.info("--- command receive to list members ---");
        List<SocioResponse> socios = repository.findAll(Sort.by(Sort.Direction.ASC, "nombre")).stream()
                .map(mapper::aRespuesta)
                .toList();
        log.info("--- finish list members: {} found ---", socios.size());
        return socios;
    }

    @Override
    @Transactional(readOnly = true)
    public SocioResponse obtener(Long id) {
        log.info("--- command receive to get member: {} ---", id);
        Socio socio = repository.findById(id).orElseThrow(() -> {
            log.warn("--- member not found: {} ---", id);
            return new RecursoNoEncontradoException("un socio", id);
        });
        log.info("--- finish get member: {} ---", id);
        return mapper.aRespuesta(socio);
    }

    @Override
    public SocioResponse crear(CrearSocioRequest request) {
        log.info("--- command receive to create member ---");
        Socio nuevo = mapper.aEntidad(request);
        if (repository.existsByEmail(nuevo.getEmail())) {
            log.warn("--- duplicated member email ---");
            throw new DatoDuplicadoException("correo", nuevo.getEmail());
        }
        Socio guardado = repository.save(nuevo);
        log.info("--- finish create member: id {} ---", guardado.getId());
        return mapper.aRespuesta(guardado);
    }
}
