package br.com.eduardo.novalumecatalogservice.dto;

import br.com.eduardo.novalumecatalogservice.model.enums.ProductCategory;

import java.util.List;

public record ProductResponseListDto(
        String id,
        String productName,
        double unitPrice,
        ProductCategory productCategory,
        List<String> imagesUrl,
        boolean featured
) {
}
