package cl.ipss.biblioteca.service.impl;

import cl.ipss.biblioteca.dto.CrearLibroRequest;
import cl.ipss.biblioteca.dto.DisponibilidadResponse;
import cl.ipss.biblioteca.dto.LibroResponse;
import cl.ipss.biblioteca.entity.Libro;
import cl.ipss.biblioteca.entity.Prestamo;
import cl.ipss.biblioteca.enums.EstadoPrestamo;
import cl.ipss.biblioteca.exception.DatoDuplicadoException;
import cl.ipss.biblioteca.exception.RecursoNoEncontradoException;
import cl.ipss.biblioteca.mapper.LibroMapper;
import cl.ipss.biblioteca.repository.LibroRepository;
import cl.ipss.biblioteca.repository.PrestamoRepository;
import cl.ipss.biblioteca.service.LibroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/** SERVICE (implementación): catálogo de libros, búsqueda y consulta de disponibilidad. */
@Service
@Transactional
public class LibroServiceImpl implements LibroService {

    private static final Logger log = LoggerFactory.getLogger(LibroServiceImpl.class);

    private final LibroRepository libroRepository;
    private final PrestamoRepository prestamoRepository;
    private final LibroMapper mapper;

    public LibroServiceImpl(LibroRepository libroRepository, PrestamoRepository prestamoRepository,
                            LibroMapper mapper) {
        this.libroRepository = libroRepository;
        this.prestamoRepository = prestamoRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LibroResponse> buscar(String texto, boolean soloDisponibles) {
        log.info("--- command receive to search books ---");
        // el texto nunca llega null al repository: "" equivale a "todo el catálogo"
        String criterio = texto == null ? "" : texto.trim();
        List<LibroResponse> libros = libroRepository.buscar(criterio, soloDisponibles).stream()
                .map(mapper::aRespuesta)
                .toList();
        log.info("--- finish search books: {} found ---", libros.size());
        return libros;
    }

    @Override
    @Transactional(readOnly = true)
    public LibroResponse obtener(Long id) {
        log.info("--- command receive to get book: {} ---", id);
        LibroResponse libro = mapper.aRespuesta(buscarPorId(id));
        log.info("--- finish get book: {} - {} ---", id, libro.titulo());
        return libro;
    }

    @Override
    @Transactional(readOnly = true)
    public DisponibilidadResponse consultarDisponibilidad(Long id) {
        log.info("--- command receive to check availability of book: {} ---", id);
        Libro libro = buscarPorId(id);
        LocalDate proxima = null;
        if (libro.getEjemplaresDisponibles() == 0) {
            // sin copias: la próxima en volver es la del préstamo activo que vence primero
            proxima = prestamoRepository.findFirstByLibroIdAndEstadoOrderByFechaVencimientoAsc(id, EstadoPrestamo.ACTIVO)
                    .map(Prestamo::getFechaVencimiento)
                    .orElse(null);
        }
        log.info("--- finish check availability of book: {} - {} available ---", id, libro.getEjemplaresDisponibles());
        return mapper.aDisponibilidad(libro, proxima);
    }

    @Override
    public LibroResponse crear(CrearLibroRequest request) {
        log.info("--- command receive to create book ---");
        String isbn = request.isbn().trim();
        if (libroRepository.existsByIsbn(isbn)) {
            log.warn("--- duplicated isbn: {} ---", isbn);
            throw new DatoDuplicadoException("ISBN", isbn);
        }
        Libro guardado = libroRepository.save(mapper.aEntidad(request));
        log.info("--- finish create book: id {} ---", guardado.getId());
        return mapper.aRespuesta(guardado);
    }

    private Libro buscarPorId(Long id) {
        return libroRepository.findById(id).orElseThrow(() -> {
            log.warn("--- book not found: {} ---", id);
            return new RecursoNoEncontradoException("un libro", id);
        });
    }
}
