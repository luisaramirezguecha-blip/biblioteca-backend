package cl.ipss.biblioteca.controller;

import cl.ipss.biblioteca.dto.CrearLibroRequest;
import cl.ipss.biblioteca.dto.DisponibilidadResponse;
import cl.ipss.biblioteca.dto.LibroResponse;
import cl.ipss.biblioteca.service.LibroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * CONTROLLER (la "C" de MVC): recibe la petición HTTP, llama al service y devuelve la respuesta.
 * No contiene lógica de negocio. Las anotaciones @Tag/@Operation documentan la API en Swagger.
 * Cada endpoint registra en info cuándo empieza, la respuesta y cuándo termina.
 */
@RestController
@RequestMapping("/api/libros")
@Tag(name = "Libros", description = "Catálogo: búsqueda de libros y consulta de disponibilidad")
public class LibroController {

    private static final Logger log = LoggerFactory.getLogger(LibroController.class);

    private final LibroService service;

    public LibroController(LibroService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Buscar libros por título, autor, ISBN o categoría (sin texto lista todo el catálogo)")
    public List<LibroResponse> buscar(HttpServletRequest requestServlet,
                                      @RequestParam(required = false) String q,
                                      @RequestParam(defaultValue = "false") boolean soloDisponibles) {
        log.info("--- start process endpoint: {} {} ---", requestServlet.getMethod(), requestServlet.getRequestURI());
        List<LibroResponse> respuesta = service.buscar(q, soloDisponibles);
        log.info("--- response search books: {} found ---", respuesta.size());
        log.info("--- end process endpoint: {} {} ---", requestServlet.getMethod(), requestServlet.getRequestURI());
        return respuesta;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un libro por id")
    public LibroResponse obtener(HttpServletRequest requestServlet, @PathVariable Long id) {
        log.info("--- start process endpoint: {} {} ---", requestServlet.getMethod(), requestServlet.getRequestURI());
        LibroResponse respuesta = service.obtener(id);
        log.info("--- response get book: {} ---", respuesta);
        log.info("--- end process endpoint: {} {} ---", requestServlet.getMethod(), requestServlet.getRequestURI());
        return respuesta;
    }

    @GetMapping("/{id}/disponibilidad")
    @Operation(summary = "Consultar la disponibilidad: copias libres y, si no quedan, cuándo vence el próximo préstamo")
    public DisponibilidadResponse disponibilidad(HttpServletRequest requestServlet, @PathVariable Long id) {
        log.info("--- start process endpoint: {} {} ---", requestServlet.getMethod(), requestServlet.getRequestURI());
        DisponibilidadResponse respuesta = service.consultarDisponibilidad(id);
        log.info("--- response availability: {} ---", respuesta);
        log.info("--- end process endpoint: {} {} ---", requestServlet.getMethod(), requestServlet.getRequestURI());
        return respuesta;
    }

    @PostMapping
    @Operation(summary = "Agregar un libro al catálogo (todas sus copias nacen disponibles)")
    @ApiResponse(responseCode = "201", description = "Libro creado")
    public ResponseEntity<LibroResponse> crear(HttpServletRequest requestServlet,
                                               @Valid @RequestBody CrearLibroRequest request) {
        log.info("--- start process endpoint: {} {} ---", requestServlet.getMethod(), requestServlet.getRequestURI());
        LibroResponse creado = service.crear(request);
        log.info("--- response create book: {} ---", creado);
        log.info("--- end process endpoint: {} {} ---", requestServlet.getMethod(), requestServlet.getRequestURI());
        return ResponseEntity.created(URI.create("/api/libros/" + creado.id())).body(creado);
    }
}
