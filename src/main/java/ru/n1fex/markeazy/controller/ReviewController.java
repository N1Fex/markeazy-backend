package ru.n1fex.markeazy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.n1fex.markeazy.dto.ReviewChangeDto;
import ru.n1fex.markeazy.dto.ReviewDto;
import ru.n1fex.markeazy.entity.Product;
import ru.n1fex.markeazy.entity.Review;
import ru.n1fex.markeazy.entity.User;
import ru.n1fex.markeazy.exception.ProductNotFoundException;
import ru.n1fex.markeazy.exception.SomethingWentWrongException;
import ru.n1fex.markeazy.mapper.ReviewMapper;
import ru.n1fex.markeazy.service.ProductService;
import ru.n1fex.markeazy.service.ReviewService;
import ru.n1fex.markeazy.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final ProductService productService;
    private final UserService userService;

    private final ReviewMapper reviewMapper;

    @GetMapping(value = "/{productId}/my", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReviewDto getMyReview(Principal principal, @PathVariable Long productId) {
        Optional<User> userOptional = userService.findByEmail(principal.getName());
        if (userOptional.isEmpty()) {
            throw new SomethingWentWrongException("Что-то пошло не так!");
        }
        Optional<Product> productOptional = productService.getProductById(productId);
        if (productOptional.isEmpty()) {
            throw new ProductNotFoundException("Товар не найден!");
        }
        return reviewMapper.toReviewDto(
                reviewService.getUserReview(userOptional.get(), productOptional.get())
        );
    }

    @GetMapping(value = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReviewDto>> getReviews(
            @PathVariable Long productId,
            @RequestParam(value="offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value="limit", required = false, defaultValue = "5") Integer limit) {
        return ResponseEntity.ok(
                reviewService.getProductReviews(productId, Math.max(offset, 0), Math.min(Math.max(limit, 3), 15)).stream()
                        .map(reviewMapper::toReviewDto)
                        .toList()
        );
    }

    @PostMapping
    public ReviewDto createReview(Principal principal, @RequestBody Review review) {
        Optional<User> userOptional = userService.findByEmail(principal.getName());
        if (userOptional.isEmpty()) {
            throw new SomethingWentWrongException("Что-то пошло не так!");
        }
        Optional<Product> productOptional = productService.getProductById(review.getProduct().getId());
        if (productOptional.isEmpty()) {
            throw new ProductNotFoundException("Товар не найден!");
        }
        Review newReview = reviewService.addReview(
                userOptional.get(),
                productOptional.get(),
                review.getMark(),
                review.getContent()
        );
        return reviewMapper.toReviewDto(newReview);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteReview(Principal principal, @RequestParam("product_id") Long id) {
        Optional<User> userOptional = userService.findByEmail(principal.getName());
        if (userOptional.isEmpty()) {
            throw new SomethingWentWrongException("Что-то пошло не так!");
        }
        reviewService.deleteReview(userOptional.get(), id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping
    public ReviewDto changeReviewText(Principal principal, @RequestBody ReviewChangeDto reviewDto) {
        Optional<User> userOptional = userService.findByEmail(principal.getName());
        if (userOptional.isEmpty()) {
            throw new SomethingWentWrongException("Что-то пошло не так!");
        }

        return reviewMapper.toReviewDto(
                reviewService.changeReviewContent(userOptional.get(), reviewDto)
        );
    }
}
