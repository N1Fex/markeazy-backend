package ru.n1fex.markeazy.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.n1fex.markeazy.entity.Product;
import ru.n1fex.markeazy.entity.Review;
import ru.n1fex.markeazy.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findReviewsByProduct_Id(Long productId, Pageable pageable);

    Review findReviewByUserAndProduct(User user, Product product);

    Optional<Review> findReviewById(Long id);
}
