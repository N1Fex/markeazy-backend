package ru.n1fex.markeazy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.n1fex.markeazy.dto.ProductCardDto;
import ru.n1fex.markeazy.dto.ProductDto;
import ru.n1fex.markeazy.entity.Product;

@Mapper(componentModel = "spring", uses = {ReviewMapper.class, SellerMapper.class})
public abstract class ProductMapper {

    @Mapping(target = "url", source = "product.objectKey")
    public abstract ProductCardDto toProductCardDto(Product product);

    @Mapping(target = "url", source = "product.objectKey")
    public abstract ProductDto toProductDto(Product product);
}