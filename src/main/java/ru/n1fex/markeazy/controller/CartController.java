package ru.n1fex.markeazy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.n1fex.markeazy.dto.CartDto;
import ru.n1fex.markeazy.entity.User;
import ru.n1fex.markeazy.exception.SomethingWentWrongException;
import ru.n1fex.markeazy.service.CartService;
import ru.n1fex.markeazy.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    @GetMapping
    public List<CartDto> getCart(Principal principal) {
        Optional<User> userOptional = userService.findByEmail(principal.getName());
        if (userOptional.isPresent()) {
            return cartService.getCart(userOptional.get());
        }
        throw new SomethingWentWrongException("Что-то пошло не так");
    }


    @PostMapping
    public List<CartDto> addProductsToCart(Principal principal, @RequestBody List<CartDto> cartDtos) {
        Optional<User> userOptional = userService.findByEmail(principal.getName());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return cartService.addProductsToCart(user, cartDtos);
        }
        throw new SomethingWentWrongException("Что-то пошло не так");
    }

    @DeleteMapping
    public Integer removeFromCart(Principal principal, @RequestParam(name="product_id") Long productId) {
        Optional<User> userOptional = userService.findByEmail(principal.getName());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return cartService.removeProductFromCart(user, productId);
        }
        throw new SomethingWentWrongException("Что-то пошло не так");
    }
}
