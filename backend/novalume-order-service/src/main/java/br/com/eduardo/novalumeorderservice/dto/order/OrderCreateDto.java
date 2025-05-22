package br.com.eduardo.novalumeorderservice.dto.order;

import br.com.eduardo.novalumeorderservice.dto.orderitem.OrderItemDto;

import java.util.List;
import java.util.UUID;

public record OrderCreateDto(
        UUID customerId,
        UUID addressId,
        UUID paymentId,
        List<OrderItemDto> items
) {
}
