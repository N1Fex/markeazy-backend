package ru.n1fex.markeazy.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    private int price;
    private int discount;
    private float rating;

    @Column(name = "reviews_count")
    private int reviewsCount;
    private int amount;
    @Column(length = 2000)
    private String description;
    private String objectKey;
    private Boolean deleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seller_id")
    private Seller seller;
}
