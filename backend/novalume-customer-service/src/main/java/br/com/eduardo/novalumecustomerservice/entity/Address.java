package br.com.eduardo.novalumecustomerservice.entity;

import br.com.eduardo.novalumecustomerservice.entity.enums.AddressType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "tb_address")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String street;
    private String neighborhood;
    private int houseNumber;
    private String zipCode;
    private AddressType addressType;
    private String city;
    private String country;

    @ManyToOne
    private Customer customer;
}