package ru.n1fex.markeazy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProductCard {

    private Long id;
    private String title;
    private int price;
    private int discount;
    private int amount;
    private float rating;
    private int reviewsCount;
    private String seller;
    private byte[] image;

    public ProductCard(Long id, String title, int price, int discount, int amount, float rating, int reviewsCount, String seller) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.discount = discount;
        this.amount = amount;
        this.rating = rating;
        this.reviewsCount = reviewsCount;
        this.seller = seller;
    }

    public ProductCard(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.price = product.getPrice();
        this.discount = product.getDiscount();
        this.amount = product.getAmount();
        this.rating = product.getRating();
        this.reviewsCount = product.getReviewsCount();
        this.seller = product.getSeller().getName();
    }
}
