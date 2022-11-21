package cat.cultura.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class RequestAlreadyAddedException extends RuntimeException {

    public RequestAlreadyAddedException() {
        super("Already added");
    }
}
