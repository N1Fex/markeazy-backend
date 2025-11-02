package ru.n1fex.markeazy.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consumer")
    private User consumer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status")
    private OrderStatus status;


    @OneToMany(mappedBy = "pk.order") //Подумать над выбором fetchType
    private List<OrderProduct> products;

}
