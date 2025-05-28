package br.com.eduardo.novalumecustomerservice.infra.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class KeycloakCreateUserException extends RuntimeException {
    public KeycloakCreateUserException(String message) {
        super(message);
    }
}
