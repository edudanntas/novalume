package br.com.eduardo.novalumecatalogservice.mapper;

import br.com.eduardo.novalumecatalogservice.dto.ProductCreateDTO;
import br.com.eduardo.novalumecatalogservice.dto.ProductUpdateDto;
import br.com.eduardo.novalumecatalogservice.model.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product mapCreateProductDtoToProductEntity(ProductCreateDTO productCreateDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProductFromDto(ProductUpdateDto productUpdateDto, @MappingTarget Product product);
}
