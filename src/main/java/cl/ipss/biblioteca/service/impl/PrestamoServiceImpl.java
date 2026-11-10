package cl.ipss.biblioteca.service.impl;

import cl.ipss.biblioteca.dto.CrearPrestamoRequest;
import cl.ipss.biblioteca.dto.PrestamoResponse;
import cl.ipss.biblioteca.entity.Libro;
import cl.ipss.biblioteca.entity.Prestamo;
import cl.ipss.biblioteca.entity.Socio;
import cl.ipss.biblioteca.enums.EstadoPrestamo;
import cl.ipss.biblioteca.exception.LibroSinEjemplaresException;
import cl.ipss.biblioteca.exception.LimitePrestamosExcedidoException;
import cl.ipss.biblioteca.exception.PrestamoDuplicadoException;
import cl.ipss.biblioteca.exception.PrestamoYaDevueltoException;
import cl.ipss.biblioteca.exception.RecursoNoEncontradoException;
import cl.ipss.biblioteca.mapper.PrestamoMapper;
import cl.ipss.biblioteca.repository.LibroRepository;
import cl.ipss.biblioteca.repository.PrestamoRepository;
import cl.ipss.biblioteca.repository.SocioRepository;
import cl.ipss.biblioteca.service.PrestamoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * SERVICE (implementación): aquí viven las reglas de la biblioteca (préstamos y devoluciones).
 * Recibe los repositories y el mapper por el constructor (inyección de dependencias).
 */
@Service
@Transactional
public class PrestamoServiceImpl implements PrestamoService {

    private static final Logger log = LoggerFactory.getLogger(PrestamoServiceImpl.class);

    /** Máximo de préstamos activos que puede tener un socio a la vez. */
    static final int LIMITE_PRESTAMOS_ACTIVOS = 3;

    /** Días que dura un préstamo antes de vencer. */
    static final int DIAS_PRESTAMO = 14;

    private final PrestamoRepository prestamoRepository;
    private final LibroRepository libroRepository;
    private final SocioRepository socioRepository;
    private final PrestamoMapper mapper;

    public PrestamoServiceImpl(PrestamoRepository prestamoRepository, LibroRepository libroRepository,
                               SocioRepository socioRepository, PrestamoMapper mapper) {
        this.prestamoRepository = prestamoRepository;
        this.libroRepository = libroRepository;
        this.socioRepository = socioRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoResponse> listar(Long socioId, EstadoPrestamo estado) {
        log.info("--- command receive to list loans ---");
        List<Prestamo> base = socioId == null
                ? prestamoRepository.findAllByOrderByIdDesc()
                : prestamoRepository.findBySocioIdOrderByIdDesc(socioId);
        List<PrestamoResponse> prestamos = base.stream()
                .filter(p -> estado == null || p.getEstado() == estado)
                .map(mapper::aRespuesta)
                .toList();
        log.info("--- finish list loans: {} found ---", prestamos.size());
        return prestamos;
    }

    @Override
    @Transactional(readOnly = true)
    public PrestamoResponse obtener(Long id) {
        log.info("--- command receive to get loan: {} ---", id);
        PrestamoResponse prestamo = mapper.aRespuesta(buscar(id));
        log.info("--- finish get loan: {} ---", id);
        return prestamo;
    }

    @Override
    public PrestamoResponse prestar(CrearPrestamoRequest request) {
        log.info("--- command receive to lend book: {} to member: {} ---", request.libroId(), request.socioId());
        Socio socio = socioRepository.findById(request.socioId()).orElseThrow(() -> {
            log.warn("--- member not found: {} ---", request.socioId());
            return new RecursoNoEncontradoException("un socio", request.socioId());
        });
        // bloquea la fila del libro hasta terminar la transacción: dos préstamos a la vez no gastan la misma copia
        Libro libro = libroRepository.buscarParaActualizar(request.libroId()).orElseThrow(() -> {
            log.warn("--- book not found: {} ---", request.libroId());
            return new RecursoNoEncontradoException("un libro", request.libroId());
        });

        // Regla 3: un socio no puede tener dos veces el mismo libro sin devolverlo.
        if (prestamoRepository.existsBySocioIdAndLibroIdAndEstado(socio.getId(), libro.getId(), EstadoPrestamo.ACTIVO)) {
            log.warn("--- duplicated loan: member {} already has book {} ---", socio.getId(), libro.getId());
            throw new PrestamoDuplicadoException(libro.getTitulo());
        }
        // Regla 2: máximo 3 préstamos activos por socio.
        if (prestamoRepository.countBySocioIdAndEstado(socio.getId(), EstadoPrestamo.ACTIVO) >= LIMITE_PRESTAMOS_ACTIVOS) {
            log.warn("--- loan limit reached: member {} ---", socio.getId());
            throw new LimitePrestamosExcedidoException(LIMITE_PRESTAMOS_ACTIVOS);
        }
        // Regla 1: debe quedar al menos una copia disponible.
        if (libro.getEjemplaresDisponibles() <= 0) {
            log.warn("--- no copies available: book {} ---", libro.getId());
            throw new LibroSinEjemplaresException(libro.getTitulo());
        }

        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() - 1);
        libroRepository.save(libro);

        Prestamo prestamo = new Prestamo();
        prestamo.setLibro(libro);
        prestamo.setSocio(socio);
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaVencimiento(LocalDate.now().plusDays(DIAS_PRESTAMO));
        prestamo.setEstado(EstadoPrestamo.ACTIVO);
        Prestamo guardado = prestamoRepository.save(prestamo);
        log.info("--- finish lend book: loan {} ---", guardado.getId());
        return mapper.aRespuesta(guardado);
    }

    @Override
    public PrestamoResponse devolver(Long id) {
        log.info("--- command receive to return loan: {} ---", id);
        Prestamo prestamo = buscar(id);

        // Regla 4: un préstamo solo se devuelve una vez.
        if (prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            log.warn("--- loan already returned: {} ---", id);
            throw new PrestamoYaDevueltoException(id);
        }

        Libro libro = libroRepository.buscarParaActualizar(prestamo.getLibro().getId()).orElseThrow(() ->
                new RecursoNoEncontradoException("un libro", prestamo.getLibro().getId()));
        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() + 1);
        libroRepository.save(libro);

        prestamo.setEstado(EstadoPrestamo.DEVUELTO);
        prestamo.setFechaDevolucion(LocalDate.now());
        Prestamo guardado = prestamoRepository.save(prestamo);
        log.info("--- finish return loan: {} ---", id);
        return mapper.aRespuesta(guardado);
    }

    private Prestamo buscar(Long id) {
        return prestamoRepository.findConDetalleById(id).orElseThrow(() -> {
            log.warn("--- loan not found: {} ---", id);
            return new RecursoNoEncontradoException("un préstamo", id);
        });
    }
}
