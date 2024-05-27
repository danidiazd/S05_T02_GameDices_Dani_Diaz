package cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.exceptions;

public class PlayerNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PlayerNotFoundException(String msg) {
        super(msg);
    }


}
