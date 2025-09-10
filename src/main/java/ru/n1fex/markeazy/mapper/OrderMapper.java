package ru.n1fex.markeazy.mapper;

import org.mapstruct.Mapper;
import ru.n1fex.markeazy.dto.OrderDto;
import ru.n1fex.markeazy.entity.Order;

@Mapper(componentModel = "spring", uses = OrderProductMapper.class)
public interface OrderMapper {

    OrderDto orderToOrderDto(Order order);

}
