package ru.n1fex.markeazy.entity.idcomposit;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
public class OrderProductId implements Serializable {

    @Column(name = "order_id")
    private Long orderId;
    @Column(name = "product_id")
    private Long productId;

}
