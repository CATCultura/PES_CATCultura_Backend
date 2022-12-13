package cat.cultura.backend.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger exceptionLogger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = { UserNotFoundException.class, TagNotFoundException.class, EventNotFoundException.class, RequestNotFoundException.class })
    public final ResponseEntity<String> handleException(Exception ex) {
        exceptionLogger.info("Entity not found. Returning corresponding 404 response. Cause: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(value = { TagAlreadyAddedException.class, TagNotPresentException.class, RequestAlreadyAddedException.class, FriendAlreadyAddedException.class, MissingRequiredParametersException.class })
    public final ResponseEntity<String> handleConflicts(Exception ex) {
        exceptionLogger.info("Business rule violated. Returning corresponding 409 response. Cause: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

}
