package cl.ipss.biblioteca.exception;

/** Se lanza cuando el socio ya tiene prestado ese mismo libro y aún no lo devuelve (HTTP 409). */
public class PrestamoDuplicadoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PrestamoDuplicadoException(String titulo) {
        super("El socio ya tiene prestado «" + titulo + "»; debe devolverlo antes de pedirlo de nuevo");
    }
}
