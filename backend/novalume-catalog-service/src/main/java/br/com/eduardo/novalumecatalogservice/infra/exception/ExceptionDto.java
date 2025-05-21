package br.com.eduardo.novalumecatalogservice.infra.exception;

import java.time.LocalDateTime;

public record ExceptionDto(
        LocalDateTime timestamp,
        int code,
        String reason
) {
}
