package br.com.eduardo.novalumecustomerservice.entity.enums;

import lombok.Getter;

@Getter
public enum AddressType {
    HOME_ADDRESS("Home Address"),
    BUSINESS_ADDRESS("Business Address");

    private final String type;

    AddressType(String type) {
        this.type = type;
    }
}
