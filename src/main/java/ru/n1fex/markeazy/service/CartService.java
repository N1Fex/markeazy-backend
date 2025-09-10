package ru.n1fex.markeazy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.n1fex.markeazy.dto.CartDto;
import ru.n1fex.markeazy.entity.User;
import ru.n1fex.markeazy.mapper.CartMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartMapper cartMapper;

    public List<CartDto> getCart(User user) {
        return user.getCartProducts().stream().map(cartMapper::toCartDto).toList();
    }

}
