package ru.n1fex.markeazy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.n1fex.markeazy.entity.OrderStatus;
import ru.n1fex.markeazy.exception.SomethingWentWrongException;
import ru.n1fex.markeazy.repository.OrderStatusRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderStatusService {

    private final OrderStatusRepository orderStatusRepository;

    public OrderStatus getOrderStatusById(int id) {
        return orderStatusRepository.findById(id)
                .orElseThrow(() -> new SomethingWentWrongException("Что-то пошло не так"));
    }

    public OrderStatus getOrderStatusByName(String name) {
        return orderStatusRepository.findByName(name)
                .orElseThrow(() -> new SomethingWentWrongException("Что-то пошло не так"));
    }

}
