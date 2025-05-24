package br.com.eduardo.novalumeorderservice.dto.order;

import br.com.eduardo.novalumeorderservice.model.enums.OrderEvent;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderMessage(
        UUID orderId,
        OrderEvent eventType,
        UUID paymentId,
        BigDecimal totalAmount

) {
}
