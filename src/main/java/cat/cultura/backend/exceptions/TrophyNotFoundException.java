package cat.cultura.backend.exceptions;

public class TrophyNotFoundException extends RuntimeException {

    public TrophyNotFoundException() {
            super("Trophy not found\n");
    }

    public TrophyNotFoundException(String message) {
            super(message);
    }

}

