package br.com.eduardo.novalumeorderservice.dto.order;

import br.com.eduardo.novalumeorderservice.model.enums.OrderPaymentType;

import java.util.UUID;

public record OrderPaymentEvent(
        UUID orderId,
        OrderPaymentType paymentType
) {
}
