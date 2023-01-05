package superapp.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class CannotProcessException extends RuntimeException {

    public CannotProcessException(){
        super();
    }

    public CannotProcessException(String message) {
        super(message);
    }
}
