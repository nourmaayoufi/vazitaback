package vazita.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private int status;
    private Date timestamp;
    private String message;
    private String details;
}