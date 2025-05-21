package br.com.eduardo.novalumecatalogservice.repository;

import br.com.eduardo.novalumecatalogservice.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for {@link ProductRepository}
 */
@DataMongoTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private Product sampleProduct;
    private Product anotherProduct;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();

        sampleProduct = new Product();
        sampleProduct.setProductName("Smartphone XYZ");
        sampleProduct.setUnitPrice(999.99);
        sampleProduct.setProductDescription("A state-of-the-art smartphone");
        sampleProduct = productRepository.save(sampleProduct);

        anotherProduct = new Product();
        anotherProduct.setProductName("Notebook ABC");
        anotherProduct.setUnitPrice(1999.99);
        anotherProduct.setProductDescription("Powerful notebook for work");
        anotherProduct = productRepository.save(anotherProduct);
    }

    @Test
    @DisplayName("Should find product by name correctly")
    void shouldFindProductByName() {
        Optional<Product> found = productRepository.getProductByProductName("Smartphone XYZ");

        assertTrue(found.isPresent());
        assertEquals(sampleProduct.getId(), found.get().getId());
        assertEquals("Smartphone XYZ", found.get().getProductName());
    }

    @Test
    @DisplayName("Should find product by ID correctly")
    void shouldFindProductById() {
        Optional<Product> found = productRepository.getProductById(sampleProduct.getId());

        assertTrue(found.isPresent());
        assertEquals(sampleProduct.getProductName(), found.get().getProductName());
    }

    @Test
    @DisplayName("Should return empty Optional when searching for non-existent name")
    void shouldReturnEmptyWhenProductNameDoesNotExist() {
        Optional<Product> found = productRepository.getProductByProductName("Non-existent Product");

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should return empty Optional when searching for non-existent ID")
    void shouldReturnEmptyWhenProductIdDoesNotExist() {
        Optional<Product> found = productRepository.getProductById("63f5a7b8d9e0c1f2a3b4c5d6");

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should distinguish between products with similar names")
    void shouldDistinguishBetweenProductsWithSimilarNames() {
        Product similarNameProduct = new Product();
        similarNameProduct.setProductName("Smartphone XYZ Pro");
        similarNameProduct.setUnitPrice(1299.99);
        productRepository.save(similarNameProduct);

        Optional<Product> found = productRepository.getProductByProductName("Smartphone XYZ");

        assertTrue(found.isPresent());
        assertEquals(sampleProduct.getId(), found.get().getId());
    }
}