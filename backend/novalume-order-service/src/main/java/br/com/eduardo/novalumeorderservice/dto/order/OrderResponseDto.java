package br.com.eduardo.novalumeorderservice.dto.order;

import br.com.eduardo.novalumeorderservice.dto.orderitem.OrderItemResponseDto;
import br.com.eduardo.novalumeorderservice.model.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponseDto(
        UUID id,
        LocalDateTime orderDate,
        String customerName,
        OrderStatus status,
        List<OrderItemResponseDto> items,
        BigDecimal totalAmount
) {
}
