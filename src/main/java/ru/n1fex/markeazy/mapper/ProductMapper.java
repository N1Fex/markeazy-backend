package ru.n1fex.markeazy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.n1fex.markeazy.dto.ProductCardDto;
import ru.n1fex.markeazy.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target="seller", expression = "java(product.getSeller().getName())")
    ProductCardDto toProductCardDto(Product product);

}
