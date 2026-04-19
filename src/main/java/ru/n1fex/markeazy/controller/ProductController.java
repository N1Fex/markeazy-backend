package ru.n1fex.markeazy.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.n1fex.markeazy.dto.ProductCardDto;
import ru.n1fex.markeazy.dto.ProductDto;
import ru.n1fex.markeazy.entity.Product;
import ru.n1fex.markeazy.exception.MissedParameterOfRequestBody;
import ru.n1fex.markeazy.exception.WrongParameterType;
import ru.n1fex.markeazy.mapper.ProductMapper;
import ru.n1fex.markeazy.service.ProductIndexingService;
import ru.n1fex.markeazy.service.ProductService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
@CrossOrigin(originPatterns = "*")
public class ProductController {

    private final ProductService productService;
    private final ProductIndexingService productIndexingService;
    private final ProductMapper productMapper;

    @PostMapping(value = "/reindexAll")
    public ResponseEntity<?> reindexProducts() {
        productIndexingService.reindexAllProducts();
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = {"", "/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductCardDto>> getProducts(@RequestParam(required = false) Integer limit) {
        return ResponseEntity.ok(productService.getRandomProducts(limit != null ? limit : 10));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDto> getProduct(@PathVariable("id") Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product
                .map(productMapper::toProductDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value="/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductCardDto>> searchProduct(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit) {

        if (limit == null || limit <= 0) {
            limit = 20;
        }

        //return ResponseEntity.ok(productService.findProductByTitleOffsetLimit(query, offset, limit));
        return ResponseEntity.ok(productService.findProductsViaElastic(query, offset, limit));
    }

    @SneakyThrows
    @PostMapping(value="/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductCardDto>> getProductsByIds(@RequestBody Map<String, Object> map) {

        if (!map.containsKey("ids")) {
            throw new MissedParameterOfRequestBody("Parameters must contain 'ids'");
        }

        if (!(map.get("ids") instanceof List<?> idsList)) {
            throw new WrongParameterType("Parameter 'ids' must be List");
        }

        List<Long> ids = idsList.stream().map(el -> Long.parseLong(el.toString())).toList();
        return ResponseEntity.ok(productService.getAllByIdIn(ids));

    }

}
