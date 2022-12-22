package superapp.logic.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class CannotProccessException extends RuntimeException{

    public CannotProccessException(){
        super();
    }

    public CannotProccessException(String message) {
        super(message);
    }
}
