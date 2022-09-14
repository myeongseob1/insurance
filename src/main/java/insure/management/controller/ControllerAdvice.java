package insure.management.controller;

import insure.management.exception.CommonErrorException;
import insure.management.exception.ErrorCode;
import insure.management.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {
    @ExceptionHandler(RuntimeException.class)
    public Object runtimeException(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(CommonErrorException.class)
    public ResponseEntity<ErrorResponse> handleEmailDuplicateException(CommonErrorException ex){
        log.error("handleEmailDuplicateException",ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, ex.getErrorCode().getStatus());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleDefaultHandlerException(Exception e){
        ErrorResponse response = new ErrorResponse(ErrorCode.REQUIRED_PARAMETER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
