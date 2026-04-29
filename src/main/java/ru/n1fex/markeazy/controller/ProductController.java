package ru.n1fex.markeazy.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.n1fex.markeazy.dto.ProductCardDto;
import ru.n1fex.markeazy.dto.ProductDto;
import ru.n1fex.markeazy.entity.Product;
import ru.n1fex.markeazy.entity.Seller;
import ru.n1fex.markeazy.exception.*;
import ru.n1fex.markeazy.mapper.ProductMapper;
import ru.n1fex.markeazy.security.AuthPrincipal;
import ru.n1fex.markeazy.service.ProductService;
import ru.n1fex.markeazy.service.SellerService;

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
    private final ProductMapper productMapper;
    private final SellerService sellerService;

    @PreAuthorize("hasRole('SELLER')")
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductDto changeProduct(
            @AuthenticationPrincipal AuthPrincipal principal,
            @PathVariable Long id,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Integer amount,
            @RequestParam(required = false) Integer price,
            @RequestParam(required = false) Integer discount
    ) throws Exception {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new ProductNotFoundException("Товар с ID = %d не найден.".formatted(id)));
        if (image != null) {
            String extension = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
            if (!extension.equals(".jpg") && !extension.equals(".png")) {
                throw new WrongFileExtensionException("Файл должен иметь расширение png или jpg.");
            }
        }
        Seller seller = sellerService
                .findById(principal.getId())
                .orElseThrow(() -> new SomethingWentWrongException("Что-то пошло не так"));
        return productMapper.toProductDto(productService.changeProduct(seller, product, title, description, amount, price, discount, image));
    }

    @PreAuthorize("hasRole('SELLER')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteProduct(@AuthenticationPrincipal AuthPrincipal authPrincipal, @PathVariable long id) {
        Seller seller = sellerService
                .findById(authPrincipal.getId())
                .orElseThrow(() -> new SomethingWentWrongException("Что-то пошло не так"));
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new ProductNotFoundException("Товар с ID = %d не найден.".formatted(id)));
        if (product.getSeller() != seller) {
            throw new AccessDeniedException("Вы не можете управлять товаром другого продавца.");
        }

        productService.deleteProduct(seller, product);
        return ResponseEntity.ok("Товар успешно удален!");
    }

    @PreAuthorize("hasRole('SELLER')")
    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductDto addProduct(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestPart("image") MultipartFile image,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam Integer amount,
            @RequestParam Integer price,
            @RequestParam Integer discount
        ) throws Exception {
        String extension = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
        if (!extension.equals(".jpg") && !extension.equals(".png")) {
            throw new WrongFileExtensionException("Файл должен иметь расширение png или jpg.");
        }
        Seller seller = sellerService
                .findById(principal.getId())
                .orElseThrow(() -> new SomethingWentWrongException("Что-то пошло не так"));
        return productMapper.toProductDto(productService.addProduct(seller, title, description, amount, price, discount, image));
    }

    @GetMapping(value = {"", "/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductCardDto>> getProducts(@RequestParam(required = false) Integer limit) {
        return ResponseEntity.ok(productService.getRandomProducts(limit != null ? limit : 10));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDto> getProduct(@PathVariable("id") Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product
                .map(p -> {
                    p.setObjectKey(productService.getPresignedUrl(p));
                    return productMapper.toProductDto(p);
                })
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
