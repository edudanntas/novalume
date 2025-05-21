package br.com.eduardo.novalumecatalogservice.dto;

import br.com.eduardo.novalumecatalogservice.model.enums.ProductCategory;

public record ProductCreateDTO(
        String productName,
        String productDescription,
        double unitPrice,
        ProductCategory productCategory,
        boolean featured
) {
}
