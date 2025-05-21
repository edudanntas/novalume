package br.com.eduardo.novalumecatalogservice.model.enums;

import lombok.Getter;

public enum ProductCategory {
    ELECTRONICS("Electronics"),
    FURNITURE("Furniture"),
    CLOTHING("Clothing"),
    BOOKS("Books"),
    TOYS("Toys"),
    SPORTS("Sports"),
    BEAUTY("Beauty"),
    FOOD("Food"),
    AUTOMOTIVE("Automotive"),
    HEALTH("Health");

    @Getter
    private final String description;

    ProductCategory(String description) {
        this.description = description;
    }
}
