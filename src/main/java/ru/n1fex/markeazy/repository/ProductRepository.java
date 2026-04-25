package ru.n1fex.markeazy.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.n1fex.markeazy.entity.Product;
import ru.n1fex.markeazy.entity.Seller;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p,s " +
            "FROM Product p " +
            "JOIN Seller s ON p.seller.id = s.id " +
            "ORDER BY RANDOM() " +
            "LIMIT :limited")
    List<Product> getRandomProductsLimited(@Param("limited") int limit);

    List<Product> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT p, s " +
            "FROM Product p " +
            "INNER JOIN Seller s ON p.seller.id = s.id " +
            "WHERE p.id IN :ids" )
    List<Product> getAllByIdIn(@Param("ids") List<Long> ids);

    List<Product> findBySellerOrderByTitle(Seller seller, Pageable pageable);
}
