package ru.n1fex.markeazy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.n1fex.markeazy.dto.OrderDto;
import ru.n1fex.markeazy.entity.User;
import ru.n1fex.markeazy.mapper.OrderMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;

    public List<OrderDto> getOrders(User user) {
        return user.getOrders().stream().map(orderMapper::orderToOrderDto).toList();
    }

}
