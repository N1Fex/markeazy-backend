package ru.n1fex.markeazy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.n1fex.markeazy.entity.Seller;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
}
