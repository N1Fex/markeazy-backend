package ru.n1fex.markeazy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String title;
    private int price;
    private int discount;
    private int amount;
    private float rating;
    private int reviewsCount;
    private Boolean deleted;
    private SellerDto seller;
    private String description;
    private String url;
}
