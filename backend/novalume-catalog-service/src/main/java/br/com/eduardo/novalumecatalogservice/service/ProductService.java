package br.com.eduardo.novalumecatalogservice.service;

import br.com.eduardo.novalumecatalogservice.dto.ProductDto;
import br.com.eduardo.novalumecatalogservice.infra.exception.custom.EntityAlreadyExists;
import br.com.eduardo.novalumecatalogservice.mapper.ProductMapper;
import br.com.eduardo.novalumecatalogservice.model.Product;
import br.com.eduardo.novalumecatalogservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    public Product createProduct(ProductDto productDto) {

        if (productRepository.getProductByProductName(productDto.productName()).isPresent()) {
            throw new EntityAlreadyExists("already Exists");
        }

        return productRepository.save(mapper.mapProductDtoToProductEntity(productDto));
    }
}
