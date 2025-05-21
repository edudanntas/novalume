package br.com.eduardo.novalumecatalogservice.infra.exception;

import br.com.eduardo.novalumecatalogservice.infra.exception.custom.EntityAlreadyExists;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityAlreadyExists.class)
    public ResponseEntity<ExceptionDto> handleEntityAlreadyExistsException(EntityAlreadyExists exception) {
        ExceptionDto ex = new ExceptionDto(LocalDateTime.now(), HttpStatus.CONFLICT.value(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex);
    }
}
