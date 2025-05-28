package br.com.eduardo.novalumecustomerservice.entity.enums;

import lombok.Getter;

@Getter
public enum CustomerType {
    NATURAL_PERSON("Natural Person"),
    LEGAL_PERSON("Legal Person");

    private final String type;

    CustomerType(String type) {
        this.type = type;
    }
}
