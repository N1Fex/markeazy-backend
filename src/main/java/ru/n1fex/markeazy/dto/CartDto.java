package ru.n1fex.markeazy.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartDto {

    private Integer quantity;
    private ProductCardDto product;

}
