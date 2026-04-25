package ru.n1fex.markeazy.mapper;

import org.mapstruct.Mapper;
import ru.n1fex.markeazy.dto.SellerDto;
import ru.n1fex.markeazy.entity.Seller;

@Mapper(componentModel = "spring")
public interface SellerMapper {
    SellerDto toSellerDto(Seller seller);
}
