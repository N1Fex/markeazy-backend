package ru.n1fex.markeazy.mapper;

import org.mapstruct.Mapper;
import ru.n1fex.markeazy.dto.CartDto;
import ru.n1fex.markeazy.entity.Cart;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface CartMapper {

    CartDto toCartDto(Cart cart);

}
