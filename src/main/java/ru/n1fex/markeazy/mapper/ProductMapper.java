package ru.n1fex.markeazy.mapper;

import org.mapstruct.Mapper;
import ru.n1fex.markeazy.dto.ProductCardDto;
import ru.n1fex.markeazy.dto.ProductDto;
import ru.n1fex.markeazy.entity.Product;

@Mapper(componentModel = "spring", uses = {ReviewMapper.class})
public interface ProductMapper {

    ProductCardDto toProductCardDto(Product product);

    ProductDto toProductDto(Product product);
}
