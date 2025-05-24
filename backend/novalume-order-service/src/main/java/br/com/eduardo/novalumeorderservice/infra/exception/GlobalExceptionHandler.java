package br.com.eduardo.novalumeorderservice.infra.exception;

import br.com.eduardo.novalumeorderservice.dto.exception.ExceptionResponseDto;
import br.com.eduardo.novalumeorderservice.infra.exception.custom.OrderNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleOrderNotFoundException(OrderNotFoundException exception, HttpServletRequest request) {
        String path = request.getRequestURI();
        ExceptionResponseDto ex = new ExceptionResponseDto(Instant.now(), HttpStatus.NOT_FOUND.value(), exception.getMessage(), path);
        return new ResponseEntity<>(ex, HttpStatus.NOT_FOUND);
    }
}
