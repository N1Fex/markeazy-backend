package ru.n1fex.markeazy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.n1fex.markeazy.entity.Product;
import ru.n1fex.markeazy.entity.ProductCard;
import ru.n1fex.markeazy.service.ProductService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
@CrossOrigin(originPatterns = "http:/*:3000")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = {"", "/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<List<ProductCard>> getProducts(@RequestParam(required = false) Integer limit) {
        return ResponseEntity.ok(productService.getRandomProducts(limit != null ? limit : 10));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value="/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductCard>> searchProduct(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit) {

        if (limit == null || limit <= 0) {
            limit = 20;
        }

        return ResponseEntity.ok(productService.findProductByTitleOffsetLimit(query, offset, limit));
    }

}
