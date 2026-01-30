package br.com.eduardo.novalumecustomerservice.infra.exception;

import java.time.LocalDateTime;

public record ExceptionDto(
        LocalDateTime timestamp,
        int code,
        String reason
) {
}
