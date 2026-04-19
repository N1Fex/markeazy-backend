package ru.n1fex.markeazy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.n1fex.markeazy.entity.Seller;

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
    private Seller seller;
    private String description;
    private byte[] image;
}
