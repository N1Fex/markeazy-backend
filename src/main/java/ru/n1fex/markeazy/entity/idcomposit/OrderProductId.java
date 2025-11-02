package ru.n1fex.markeazy.entity.idcomposit;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.n1fex.markeazy.entity.Order;
import ru.n1fex.markeazy.entity.Product;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
public class OrderProductId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
