package br.com.eduardo.novalumeorderservice.dto.product;

public record ProductDto(
        String id,
        String productName,
        double unitPrice,
        boolean sellIndicator
) {
}
