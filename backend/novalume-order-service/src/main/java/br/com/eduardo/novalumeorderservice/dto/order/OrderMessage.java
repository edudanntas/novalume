package br.com.eduardo.novalumeorderservice.dto.order;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderMessage(
        UUID id,
        String status,
        BigDecimal totalAmount
) {
}
