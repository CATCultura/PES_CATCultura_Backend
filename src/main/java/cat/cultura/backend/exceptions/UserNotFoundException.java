package cat.cultura.backend.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("User not found\n");
    }

    public UserNotFoundException(String message) {
        super(message);
    }

}
