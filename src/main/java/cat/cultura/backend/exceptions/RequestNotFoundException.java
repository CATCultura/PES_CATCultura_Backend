package cat.cultura.backend.exceptions;

public class RequestNotFoundException extends RuntimeException{

    public RequestNotFoundException() {
        super("Request not found");
    }

    public RequestNotFoundException(String message) {
        super(message);
    }

}
