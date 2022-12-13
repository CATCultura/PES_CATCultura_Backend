package cat.cultura.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class TagNotPresentException extends RuntimeException {

    public TagNotPresentException() {
        super("Tag not associated to user");
    }
}
