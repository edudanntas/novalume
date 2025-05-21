package br.com.eduardo.novalumecatalogservice.mapper;

import br.com.eduardo.novalumecatalogservice.dto.ProductCreateDTO;
import br.com.eduardo.novalumecatalogservice.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product mapCreateProductDtoToProductEntity(ProductCreateDTO productCreateDTO);
}
