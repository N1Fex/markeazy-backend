package ru.n1fex.markeazy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.n1fex.markeazy.dto.OrderDto;
import ru.n1fex.markeazy.entity.User;
import ru.n1fex.markeazy.exception.SomethingWentWrongException;
import ru.n1fex.markeazy.service.OrderService;
import ru.n1fex.markeazy.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    public List<OrderDto> getOrders(Principal principal) {
        Optional<User> userOptional = userService.findByEmail(principal.getName());
        if (userOptional.isPresent()) {
            return orderService.getOrders(userOptional.get());
        }
        throw new SomethingWentWrongException("Что-то пошло не так");
    }

}
