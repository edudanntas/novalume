package br.com.eduardo.novalumecatalogservice.repository;

import br.com.eduardo.novalumecatalogservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> getProductByProductName(String productName);

    Optional<Product> getProductById(String id);
}
