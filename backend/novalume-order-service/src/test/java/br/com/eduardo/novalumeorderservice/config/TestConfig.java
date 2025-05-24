package br.com.eduardo.novalumeorderservice.config;

import br.com.eduardo.novalumeorderservice.dto.product.ProductDto;
import br.com.eduardo.novalumeorderservice.service.clients.ProductCatalogClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public ProductCatalogClient mockProductCatalogClient() {
        return new ProductCatalogClient() {
            @Override
            public ProductDto getProductById(String id) {
                return new ProductDto(id, "Test Product", 99.99, true);
            }
        };
    }
}
