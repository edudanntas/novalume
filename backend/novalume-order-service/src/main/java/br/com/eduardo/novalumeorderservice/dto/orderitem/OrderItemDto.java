package br.com.eduardo.novalumeorderservice.dto.orderitem;

public record OrderItemDto(
        String productId,
        Integer quantity
) {
}
