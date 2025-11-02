package ru.n1fex.markeazy.entity;

import jakarta.persistence.*;
import lombok.Data;
import ru.n1fex.markeazy.entity.idcomposit.OrderProductId;

@Entity
@Table(name = "orders_products")
@Data
public class OrderProduct {

    @EmbeddedId
    private OrderProductId pk;

    private Integer quantity;
    private Integer price;

}
