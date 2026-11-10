package cl.ipss.biblioteca.controller;

import cl.ipss.biblioteca.dto.CrearPrestamoRequest;
import cl.ipss.biblioteca.dto.PrestamoResponse;
import cl.ipss.biblioteca.enums.EstadoPrestamo;
import cl.ipss.biblioteca.service.PrestamoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
@Tag(name = "Préstamos", description = "Préstamos y devoluciones de libros")
public class PrestamoController {

    private static final Logger log = LoggerFactory.getLogger(PrestamoController.class);

    private final PrestamoService service;

    public PrestamoController(PrestamoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar préstamos (filtros opcionales: socioId y estado ACTIVO/DEVUELTO)")
    public List<PrestamoResponse> listar(HttpServletRequest requestServlet,
                                         @RequestParam(required = false) Long socioId,
                                         @RequestParam(required = false) EstadoPrestamo estado) {
        log.info("--- start process endpoint: {} {} ---", requestServlet.getMethod(), requestServlet.getRequestURI());
        List<PrestamoResponse> respuesta = service.listar(socioId, estado);
        log.info("--- response list loans: {} found ---", respuesta.size());
        log.info("--- end process endpoint: {} {} ---", requestServlet.getMethod(), requestServlet.getRequestURI());
        return respuesta;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un préstamo por id")
    public PrestamoResponse obtener(HttpServletRequest requestServlet, @PathVariable Long id) {
        log.info("--- start process endpoint: {} {} ---", requestServlet.getMethod(), requestServlet.getRequestURI());
        PrestamoResponse respuesta = service.obtener(id);
        log.info("--- response get loan: {} ---", respuesta);
        log.info("--- end process endpoint: {} {} ---", requestServlet.getMethod(), requestServlet.getRequestURI());
        return respuesta;
    }

    @PostMapping
    @Operation(summary = "Realizar un préstamo (máx. 3 activos por socio, 14 días, requiere copia disponible)")
    @ApiResponse(responseCode = "201", description = "Préstamo realizado")
    public ResponseEntity<PrestamoResponse> prestar(HttpServletRequest requestServlet,
                                                    @Valid @RequestBody CrearPrestamoRequest request) {
        log.info("--- start process endpoint: {} {} ---", requestServlet.getMethod(), requestServlet.getRequestURI());
        PrestamoResponse creado = service.prestar(request);
        log.info("--- response lend book: {} ---", creado);
        log.info("--- end process endpoint: {} {} ---", requestServlet.getMethod(), requestServlet.getRequestURI());
        return ResponseEntity.created(URI.create("/api/prestamos/" + creado.id())).body(creado);
    }

    @PatchMapping("/{id}/devolucion")
    @Operation(summary = "Registrar la devolución de un préstamo (libera la copia)")
    public PrestamoResponse devolver(HttpServletRequest requestServlet, @PathVariable Long id) {
        log.info("--- start process endpoint: {} {} ---", requestServlet.getMethod(), requestServlet.getRequestURI());
        PrestamoResponse respuesta = service.devolver(id);
        log.info("--- response return loan: {} ---", respuesta);
        log.info("--- end process endpoint: {} {} ---", requestServlet.getMethod(), requestServlet.getRequestURI());
        return respuesta;
    }
}
