package cat.cultura.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class TagAlreadyAddedException extends RuntimeException {

    public TagAlreadyAddedException() {super("Tag already added for user");}
}
