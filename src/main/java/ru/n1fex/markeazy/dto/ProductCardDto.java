package ru.n1fex.markeazy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductCardDto {

    private Long id;
    private String title;
    private int price;
    private int discount;
    private int amount;
    private float rating;
    private int reviewsCount;
    private SellerDto seller;
    private Boolean deleted;
    private String url;

}
