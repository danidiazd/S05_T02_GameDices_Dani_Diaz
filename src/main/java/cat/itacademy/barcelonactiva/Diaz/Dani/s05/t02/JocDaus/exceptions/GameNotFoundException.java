package cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.exceptions;

public class GameNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public GameNotFoundException(String msg) {
        super(msg);
    }
}
