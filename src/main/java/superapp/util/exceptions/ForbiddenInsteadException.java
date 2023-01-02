package superapp.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class ForbiddenInsteadException extends RuntimeException {

    public ForbiddenInsteadException() {
        super();
    }

    public ForbiddenInsteadException(String message) {
        super(message);
    }
}
