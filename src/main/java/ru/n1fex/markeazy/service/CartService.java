package ru.n1fex.markeazy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.n1fex.markeazy.dto.CartDto;
import ru.n1fex.markeazy.entity.Cart;
import ru.n1fex.markeazy.entity.Product;
import ru.n1fex.markeazy.entity.User;
import ru.n1fex.markeazy.entity.idcomposit.CartId;
import ru.n1fex.markeazy.mapper.CartMapper;
import ru.n1fex.markeazy.mapper.ProductMapper;
import ru.n1fex.markeazy.repository.CartRepository;
import ru.n1fex.markeazy.repository.ProductRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartMapper cartMapper;
    private final ProductMapper productMapper;

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    private Cart createCart(User user, CartDto cartDto, Product product) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(cartDto.getQuantity());

        CartId cartId = new CartId();
        cartId.setProductId(product.getId());
        cartId.setUserId(user.getId());

        cart.setId(cartId);
        return cart;
    }

    public List<CartDto> getCart(User user) {
        return user.getCartProducts().stream().map(cartMapper::toCartDto).toList();
    }

    @Transactional
    public List<CartDto> addProductsToCart(User user, List<CartDto> cartDtos) {

        List<Cart> carts = cartDtos.stream()
                .map(cartDto -> {
                    Optional<Product> productOptional = productRepository.findById(cartDto.getProduct().getId());

                    if (productOptional.isPresent()) {
                        Product product = productOptional.get();
                        return createCart(user, cartDto, product);
                    }
                    return null;
                }).filter(Objects::nonNull).toList();

        return cartRepository.saveAll(carts).stream().map(cartMapper::toCartDto).toList();
    }


    @Transactional
    public Integer removeProductFromCart(User user, Long productId) {
            return cartRepository.deleteCartByUser_IdAndProduct_Id(user.getId(), productId);
    }
}
