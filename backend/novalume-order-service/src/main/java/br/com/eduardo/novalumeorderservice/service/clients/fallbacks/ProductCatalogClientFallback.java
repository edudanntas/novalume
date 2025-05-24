package br.com.eduardo.novalumeorderservice.service.clients.fallbacks;

import br.com.eduardo.novalumeorderservice.dto.product.ProductDto;
import br.com.eduardo.novalumeorderservice.infra.exception.custom.ProductCatalogUnavailableException;
import br.com.eduardo.novalumeorderservice.service.clients.ProductCatalogClient;
import org.springframework.stereotype.Component;

@Component
public class ProductCatalogClientFallback implements ProductCatalogClient {

    @Override
    public ProductDto getProductById(String id) {
        throw new ProductCatalogUnavailableException("Catalog is unavailable at the moment");
    }
}
