package cl.ipss.biblioteca.exception;

/** Se lanza al crear un libro con un ISBN repetido o un socio con un correo repetido (HTTP 409). */
public class DatoDuplicadoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DatoDuplicadoException(String campo, String valor) {
        super("Ya existe un registro con " + campo + " " + valor);
    }
}
