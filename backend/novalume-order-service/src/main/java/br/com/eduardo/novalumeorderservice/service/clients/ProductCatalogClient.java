package br.com.eduardo.novalumeorderservice.service.clients;

import br.com.eduardo.novalumeorderservice.dto.product.ProductDto;
import br.com.eduardo.novalumeorderservice.service.clients.fallbacks.ProductCatalogClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-catalog", url = "${services.product-catalog.url}", fallback = ProductCatalogClientFallback.class)
public interface ProductCatalogClient {
    @GetMapping("/{id}")
    ProductDto getProductById(@PathVariable String id);
}
