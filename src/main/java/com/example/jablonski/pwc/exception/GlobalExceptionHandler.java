package com.example.jablonski.pwc.exception;

import com.example.jablonski.pwc.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler({NoPathFoundException.class, CountryNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleException(RuntimeException exception){
        log.error(exception.getMessage());
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(exception.getMessage(), LocalDateTime.now().toString()));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleException(Exception exception){
        log.error(exception.getMessage());
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(exception.getMessage(), LocalDateTime.now().toString()));
    }
}
