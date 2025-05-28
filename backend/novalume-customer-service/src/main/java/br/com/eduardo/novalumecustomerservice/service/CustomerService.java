package br.com.eduardo.novalumecustomerservice.service;

import br.com.eduardo.novalumecustomerservice.dto.customer.CreateCustomerDto;
import br.com.eduardo.novalumecustomerservice.entity.Customer;
import br.com.eduardo.novalumecustomerservice.repository.CustomerRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final KeycloakService keycloakService;
    private final CustomerRepository customerRepository;

    @Transactional
    public Customer createCustomer(CreateCustomerDto customerDto) {
        Optional<Customer> existingCustomer = customerRepository.findCustomerByEmail(customerDto.email());

        if (existingCustomer.isPresent()) throw new EntityExistsException("Customer already exists");


        String keycloakUserId = keycloakService.createKeycloakUser(customerDto.firstName(), customerDto.lastName(), customerDto.email(),
                customerDto.password(), "CUSTOMER");

        Customer customer = new Customer();
        customer.setFirstName(customerDto.firstName());
        customer.setLastName(customerDto.lastName());
        customer.setEmail(customerDto.email());
        customer.setDocumentNumber(customerDto.documentNumber());
        customer.setCustomerType(customerDto.customerType());
        customer.setKeycloakUserId(keycloakUserId);
        return customerRepository.save(customer);
    }

    public Customer getCustomerById(String keycloakUserId){
        Optional<Customer> customer = customerRepository.findCustomerByKeycloakUserId(keycloakUserId);

        if (customer.isEmpty()) throw new EntityNotFoundException("Cannot find customer with id: " + keycloakUserId);

        return customer.get();
    }
}
