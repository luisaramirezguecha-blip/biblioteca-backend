package cl.ipss.biblioteca.exception;

/** Se lanza al prestar un libro cuyas copias están todas prestadas (HTTP 409). */
public class LibroSinEjemplaresException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LibroSinEjemplaresException(String titulo) {
        super("No quedan ejemplares disponibles de «" + titulo + "»; espera a que devuelvan uno");
    }
}
