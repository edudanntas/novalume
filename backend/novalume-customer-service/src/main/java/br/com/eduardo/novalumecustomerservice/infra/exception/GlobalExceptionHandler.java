package br.com.eduardo.novalumecustomerservice.infra.exception;

import br.com.eduardo.novalumecustomerservice.infra.exception.custom.KeycloakCreateUserException;
import jakarta.persistence.EntityExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ExceptionDto> handleEntityExistsException(EntityExistsException exception){
        ExceptionDto exceptionDto = new ExceptionDto(LocalDateTime.now(), HttpStatus.CONFLICT.value(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionDto);
    }

    @ExceptionHandler(KeycloakCreateUserException.class)
    public ResponseEntity<ExceptionDto> handleKeycloakCreateUserException(KeycloakCreateUserException exception){
        ExceptionDto exceptionDto = new ExceptionDto(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDto);
    }
}
