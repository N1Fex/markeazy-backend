package ru.n1fex.markeazy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.n1fex.markeazy.dto.ProductDto;
import ru.n1fex.markeazy.dto.SellerDto;
import ru.n1fex.markeazy.entity.Seller;
import ru.n1fex.markeazy.exception.SellerNotFoundException;
import ru.n1fex.markeazy.mapper.ProductMapper;
import ru.n1fex.markeazy.mapper.SellerMapper;
import ru.n1fex.markeazy.security.AuthPrincipal;
import ru.n1fex.markeazy.service.SellerService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
@CrossOrigin(originPatterns = "*")
public class SellerController {

    private final SellerService sellerService;
    private final SellerMapper sellerMapper;
    private final ProductMapper productMapper;

    @GetMapping("/{id}")
    public SellerDto getSeller(@PathVariable Long id) {
        return sellerService.findById(id)
                .map(sellerMapper::toSellerDto)
                .map(d -> {
                    d.setLogin("");
                    return d;
                })
                .orElseThrow(() -> new SellerNotFoundException("Продавец не найден!"));
    }

    @GetMapping("/{id}/products")
    public List<ProductDto> getSellerProducts(
            @PathVariable Long id,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit
    ) {
        Optional<Seller> sellerOpt = sellerService.findById(id);
        if (sellerOpt.isEmpty()) {
            throw new SellerNotFoundException("Продавец не найден!");
        }
        return sellerService.getProducts(sellerOpt.get(), Math.max(offset, 0), Math.min(Math.max(limit, 3), 20))
                .stream()
                .map(productMapper::toProductDto)
                .toList();
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> getCurrentSeller(@AuthenticationPrincipal AuthPrincipal principal) {
        return sellerService.findById(principal.getId())
                .<ResponseEntity<?>>map(seller -> ResponseEntity.ok(new SellerDto(
                        seller.getId(),
                        seller.getLogin(),
                        seller.getName(),
                        seller.getRegistrationDate()
                )))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
