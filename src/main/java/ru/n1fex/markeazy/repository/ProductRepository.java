package ru.n1fex.markeazy.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.n1fex.markeazy.entity.Product;
import ru.n1fex.markeazy.entity.ProductCard;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT new ru.n1fex.markeazy.entity.ProductCard(p.id, p.title, p.price, p.discount, p.amount, p.rating, p.reviewsCount, p.seller.name) " +
            "FROM Product p " +
            "WHERE (p.id < 898) " +
            "ORDER BY RANDOM() " +
            "LIMIT :limited")
    List<ProductCard> getRandomProductsLimited(@Param("limited") int limit);

    List<Product> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
