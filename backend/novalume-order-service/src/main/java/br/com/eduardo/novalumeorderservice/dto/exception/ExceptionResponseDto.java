package br.com.eduardo.novalumeorderservice.dto.exception;

import java.time.Instant;

public record ExceptionResponseDto(
        Instant timestamp,
        int code,
        String reason,
        String path
) {
}
