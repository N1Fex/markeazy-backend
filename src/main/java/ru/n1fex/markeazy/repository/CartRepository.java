package ru.n1fex.markeazy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.n1fex.markeazy.entity.Cart;
import ru.n1fex.markeazy.entity.idcomposit.CartId;

@Repository
public interface CartRepository extends JpaRepository<Cart, CartId> {
}
