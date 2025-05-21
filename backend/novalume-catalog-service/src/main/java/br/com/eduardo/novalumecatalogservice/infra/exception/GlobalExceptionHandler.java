package br.com.eduardo.novalumecatalogservice.infra.exception;

import br.com.eduardo.novalumecatalogservice.infra.exception.custom.EntityAlreadyExistsException;
import br.com.eduardo.novalumecatalogservice.infra.exception.custom.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ExceptionDto> handleEntityAlreadyExistsException(EntityAlreadyExistsException exception) {
        ExceptionDto ex = new ExceptionDto(LocalDateTime.now(), HttpStatus.CONFLICT.value(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleEntityNotFoundException(EntityNotFoundException exception){
        ExceptionDto ex = new ExceptionDto(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDto> handleIllegalArgumentException(IllegalArgumentException exception){
        ExceptionDto ex = new ExceptionDto(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
    }
}
