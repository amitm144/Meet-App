package superapp.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class ForbbidenOperationException extends RuntimeException {

    public ForbbidenOperationException() {
        super();
    }

    public ForbbidenOperationException(String message) {
        super(message);
    }
}
