package insure.management.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class CommonResponse<T> {
    private HttpStatus status;
    private String message;
    private int code;
    private T data;

}
