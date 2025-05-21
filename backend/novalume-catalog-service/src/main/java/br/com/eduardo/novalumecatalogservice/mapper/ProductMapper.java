package br.com.eduardo.novalumecatalogservice.mapper;

import br.com.eduardo.novalumecatalogservice.dto.ProductDto;
import br.com.eduardo.novalumecatalogservice.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product mapProductDtoToProductEntity(ProductDto productDto);

    ProductDto mapProductEntityToProductDto(Product product);
}
