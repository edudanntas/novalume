package br.com.eduardo.novalumecatalogservice.dto;

import br.com.eduardo.novalumecatalogservice.model.enums.ProductCategory;

import java.util.List;

public record ProductUpdateDto(
        String productName,
        String productDescription,
        Double unitPrice,
        Boolean sellIndicator,
        ProductCategory productCategory,
        List<String> imagesUrl,
        Boolean featured
) {
}
