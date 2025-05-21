package br.com.eduardo.novalumecatalogservice.dto;

import br.com.eduardo.novalumecatalogservice.model.enums.ProductCategory;

import java.util.List;
import java.util.Optional;

public record ProductUpdateDto(
        Optional<String> productName,
        Optional<String> productDescription,
        Optional<Double> unitPrice,
        Optional<Boolean> sellIndicator,
        Optional<ProductCategory> productCategory,
        Optional<List<String>> imagesUrl,
        Optional<Boolean> featured
) {
}
