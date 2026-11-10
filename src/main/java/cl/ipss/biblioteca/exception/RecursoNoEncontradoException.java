package cl.ipss.biblioteca.exception;

/** Se lanza cuando no existe el libro, socio o préstamo pedido (HTTP 404). */
public class RecursoNoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RecursoNoEncontradoException(String recurso, Long id) {
        super("No existe " + recurso + " con id " + id);
    }
}
