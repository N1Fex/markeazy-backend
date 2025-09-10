package ru.n1fex.markeazy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
