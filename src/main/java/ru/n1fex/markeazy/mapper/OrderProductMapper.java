package ru.n1fex.markeazy.mapper;

import org.mapstruct.Mapper;
import ru.n1fex.markeazy.dto.OrderProductDto;
import ru.n1fex.markeazy.entity.OrderProduct;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderProductMapper {

    OrderProductDto toOrderProductDto(OrderProduct orderProduct);

}
