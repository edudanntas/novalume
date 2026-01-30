package br.com.eduardo.novalumecustomerservice.dto.customer;

import br.com.eduardo.novalumecustomerservice.entity.enums.CustomerType;

public record CreateCustomerDto(
        String firstName,
        String lastName,
        String password,
        String email,
        String documentNumber,
        CustomerType customerType
) {
}
