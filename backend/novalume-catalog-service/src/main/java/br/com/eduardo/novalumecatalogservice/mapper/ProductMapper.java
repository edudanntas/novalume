package br.com.eduardo.novalumecatalogservice.mapper;

import br.com.eduardo.novalumecatalogservice.dto.ProductCreateDTO;
import br.com.eduardo.novalumecatalogservice.dto.ProductResponseListDto;
import br.com.eduardo.novalumecatalogservice.model.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product mapCreateProductDtoToProductEntity(ProductCreateDTO productCreateDTO);

    List<ProductResponseListDto> mapProductsListToProductListDto(List<Product> products);
}
