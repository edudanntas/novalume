package br.com.eduardo.novalumecustomerservice.controller;

import br.com.eduardo.novalumecustomerservice.dto.customer.CreateCustomerDto;
import br.com.eduardo.novalumecustomerservice.dto.customer.CustomerLoginDto;
import br.com.eduardo.novalumecustomerservice.entity.Customer;
import br.com.eduardo.novalumecustomerservice.infra.security.custom.OwnerOnly;
import br.com.eduardo.novalumecustomerservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping(value = "/{id}")
    @OwnerOnly
    public ResponseEntity<Customer> getCustomerById(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getCustomerById(id));
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody CustomerLoginDto customerLoginDto){
        return ResponseEntity.status(HttpStatus.OK).headers(customerService.authenticate(customerLoginDto)).build();
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Customer> createCustomer(@RequestBody CreateCustomerDto customerDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(customerDto));
    }
}
