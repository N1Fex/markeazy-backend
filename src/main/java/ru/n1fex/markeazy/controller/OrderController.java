package ru.n1fex.markeazy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.n1fex.markeazy.dto.OrderDto;
import ru.n1fex.markeazy.dto.OrderProductDto;
import ru.n1fex.markeazy.dto.OrderProductPlacementDto;
import ru.n1fex.markeazy.entity.User;
import ru.n1fex.markeazy.exception.EmptyOrderCartException;
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

    @GetMapping("/{orderId}/products")
    public List<OrderProductDto> getOrderProducts(Principal principal, @PathVariable("orderId") Long orderId) {
        Optional<User> userOptional = userService.findByEmail(principal.getName());
        if (userOptional.isPresent()) {
            return orderService.getOrderProducts(orderId, userOptional.get().getId());
        }
        throw new SomethingWentWrongException("Что-то пошло не так");
    }

    @GetMapping
    public List<OrderDto> getOrders(Principal principal, @RequestParam("page") Integer page) {
        Optional<User> userOptional = userService.findByEmail(principal.getName());
        if (userOptional.isPresent()) {
            return orderService.getOrdersByConsumer(userOptional.get(), page);
        }
        throw new SomethingWentWrongException("Что-то пошло не так");
    }

    @PostMapping
    public OrderDto createOrder(Principal principal, @RequestBody List<OrderProductPlacementDto> products) {
        Optional<User> userOptional = userService.findByEmail(principal.getName());
        if (products.isEmpty()) {
            throw new EmptyOrderCartException("Невозможно оформить заказ с пустым списком товаров");
        }
        if (userOptional.isPresent()) {
            return orderService.createOrder(userOptional.get(), products);
        }
        throw new SomethingWentWrongException("Что-то пошло не так");
    }

}
