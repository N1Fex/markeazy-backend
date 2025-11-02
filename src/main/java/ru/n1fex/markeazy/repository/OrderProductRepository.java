package ru.n1fex.markeazy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.n1fex.markeazy.entity.OrderProduct;
import ru.n1fex.markeazy.entity.idcomposit.OrderProductId;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProductId> {

}
