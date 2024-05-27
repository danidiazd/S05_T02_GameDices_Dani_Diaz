package cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.exceptions;

public class PlayerAlreadyExistException extends RuntimeException {

    private static long serialVersionUID = 1L;

    public PlayerAlreadyExistException(String msg) {
        super(msg);
    }
}
