package cl.ipss.biblioteca.exception;

/** Se lanza al devolver un préstamo que ya estaba devuelto (HTTP 409). */
public class PrestamoYaDevueltoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PrestamoYaDevueltoException(Long id) {
        super("El préstamo " + id + " ya fue devuelto");
    }
}
