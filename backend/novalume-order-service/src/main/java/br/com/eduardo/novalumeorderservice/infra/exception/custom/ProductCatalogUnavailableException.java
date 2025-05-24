package br.com.eduardo.novalumeorderservice.infra.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ProductCatalogUnavailableException extends RuntimeException {
    public ProductCatalogUnavailableException(String message) {
        super(message);
    }
}
