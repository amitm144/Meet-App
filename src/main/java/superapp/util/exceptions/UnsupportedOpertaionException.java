package superapp.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UnsupportedOpertaionException extends RuntimeException {

    public UnsupportedOpertaionException() {
        super();
    }

    public UnsupportedOpertaionException(String message) {
            super(message);
    }

}
