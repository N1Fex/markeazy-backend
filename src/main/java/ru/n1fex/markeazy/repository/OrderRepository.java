package ru.n1fex.markeazy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.n1fex.markeazy.entity.Order;
import ru.n1fex.markeazy.entity.OrderProduct;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT o.id, o.date, os.id, os.name, os.description, count(op.product_id) as amount, sum(op.price) as totalSum " +
            "FROM orders o " +
            "INNER JOIN orders_products op ON o.id = op.order_id " +
            "INNER JOIN order_status os ON o.status = os.id " +
            "WHERE o.consumer = :userId " +
            "GROUP BY o.id, os.id, o.date " +
            "ORDER BY o.date DESC " +
            "LIMIT 5 OFFSET :indent",
        nativeQuery = true)
    List<Object[]> findByConsumer(@Param("userId") Long userId, @Param("indent") Integer offset);

}
