package cat.cultura.backend.exceptions;

public class RequestNotFoundException extends RuntimeException{

    public RequestNotFoundException() {
        super("Request not found\n");
    }

    public RequestNotFoundException(String message) {
        super(message);
    }

}
