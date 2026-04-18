package ru.n1fex.markeazy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.n1fex.markeazy.dto.ReviewChangeDto;
import ru.n1fex.markeazy.entity.Product;
import ru.n1fex.markeazy.entity.Review;
import ru.n1fex.markeazy.entity.User;
import ru.n1fex.markeazy.exception.ReviewNotFoundException;
import ru.n1fex.markeazy.exception.SomethingWentWrongException;
import ru.n1fex.markeazy.repository.ProductRepository;
import ru.n1fex.markeazy.repository.ReviewRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public List<Review> getProductReviews(Long productId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return reviewRepository.findReviewsByProduct_Id(productId, pageable);
    }

    public Review getUserReview(User user, Product product) {
        return reviewRepository.findReviewByUserAndProduct(user, product);
    }

    @Transactional
    public Review addReview(User user, Product product, Integer mark, String reviewText) {

        Review existed = reviewRepository.findReviewByUserAndProduct(user, product);
        if (existed != null) {
            throw new SomethingWentWrongException("Вы не можете написать два отзыва об одном товаре!");
        }
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setMark(mark);
        review.setContent(reviewText);
        review.setDate(new Date());

        int reviewCount = product.getReviewsCount();
        product.setRating((product.getRating() * reviewCount + mark)/ (reviewCount + 1));
        product.setReviewsCount(reviewCount + 1);

        productRepository.save(product);
        return reviewRepository.save(review);
    }

    @Transactional
    public void deleteReview(User user, Long id) {
        Optional<Review> reviewOpt = reviewRepository.findReviewById(id);
        Review review = reviewOpt.orElseThrow(() -> new ReviewNotFoundException("Похоже что отзыва не существует!"));
        if (!review.getUser().equals(user)) {
            throw new AccessDeniedException("Нет доступа к редактированию чужого отзыва!");
        }
        Product product = review.getProduct();
        int reviewCount = product.getReviewsCount();
        product.setRating((product.getRating() * reviewCount - review.getMark()) / (reviewCount - 1));
        product.setReviewsCount(reviewCount - 1);
        productRepository.save(product);
        reviewRepository.delete(review);
    }

    public Review changeReviewContent(User user, ReviewChangeDto dto) {
        Optional<Review> reviewOpt = reviewRepository.findReviewById(dto.getId());
        Review review = reviewOpt.orElseThrow(() -> new ReviewNotFoundException("Похоже что отзыва не существует!"));
        if (!review.getUser().equals(user)) {
            throw new AccessDeniedException("Нет доступа к редактированию чужого отзыва!");
        }
        String content = dto.getContent();
        Integer mark = dto.getMark();
        if (content != null) {
            review.setContent(content);
        }
        if (mark != null) {
            review.setMark(mark);
        }
        return reviewRepository.save(review);
    }
}
