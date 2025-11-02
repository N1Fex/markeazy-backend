package ru.n1fex.markeazy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.n1fex.markeazy.dto.OrderProductDto;
import ru.n1fex.markeazy.entity.OrderProduct;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderProductMapper {
    
    @Mapping(source = "pk.product", target="product")
    OrderProductDto toOrderProductDto(OrderProduct orderProduct);

}
