package ru.n1fex.markeazy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.n1fex.markeazy.entity.idcomposit.CartId;

@Entity
@Table(name = "cart")
@Data
@NoArgsConstructor
public class Cart {

    @EmbeddedId
    private CartId id;

    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("productId")
    private Product product;


}
