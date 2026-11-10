package cl.ipss.biblioteca.exception;

/** Se lanza cuando el socio ya tiene el máximo de préstamos activos a la vez (HTTP 409). */
public class LimitePrestamosExcedidoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LimitePrestamosExcedidoException(int limite) {
        super("El socio ya tiene el máximo de " + limite
                + " préstamos activos; devuelve un libro antes de pedir otro");
    }
}
