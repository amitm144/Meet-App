package superapp.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class ThridPartyAPIException extends RuntimeException {

    public ThridPartyAPIException(){
        super();
    }

    public ThridPartyAPIException(String message) {
        super(message);
    }
}