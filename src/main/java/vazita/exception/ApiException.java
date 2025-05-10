package vazita.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
    
    @Getter
    private final HttpStatus status;
    
    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    
    public ApiException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}
