package ru.n1fex.markeazy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.n1fex.markeazy.dto.OrderDto;
import ru.n1fex.markeazy.entity.User;
import ru.n1fex.markeazy.mapper.OrderMapper;
import ru.n1fex.markeazy.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public Optional<OrderDto> findById(Long id) {
        return orderRepository.findById(id).map(orderMapper::orderToOrderDto);
    }

    public List<OrderDto> getOrders(User user) {
        return user.getOrders().stream().map(orderMapper::orderToOrderDto).toList();
    }

}
