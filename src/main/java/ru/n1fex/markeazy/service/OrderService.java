package ru.n1fex.markeazy.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.n1fex.markeazy.dto.OrderDto;
import ru.n1fex.markeazy.dto.OrderProductDto;
import ru.n1fex.markeazy.dto.OrderProductPlacementDto;
import ru.n1fex.markeazy.entity.*;
import ru.n1fex.markeazy.entity.idcomposit.OrderProductId;
import ru.n1fex.markeazy.exception.SomethingWentWrongException;
import ru.n1fex.markeazy.mapper.OrderMapper;
import ru.n1fex.markeazy.mapper.OrderProductMapper;
import ru.n1fex.markeazy.repository.OrderProductRepository;
import ru.n1fex.markeazy.repository.OrderRepository;
import ru.n1fex.markeazy.repository.ProductRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderProductMapper orderProductMapper;

    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderRepository orderRepository;
    private final OrderStatusService orderStatusService;

    @SneakyThrows
    public List<OrderProductDto> getOrderProducts(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new SomethingWentWrongException("Что-то пошло не так..."));

        if (!order.getConsumer().getId().equals(userId)) {
            throw new InsufficientAuthenticationException("Нельзя посмотреть чужой заказ");
        }

        return order.getProducts().stream().map(orderProductMapper::toOrderProductDto).toList();
    }

    public List<OrderDto> getOrdersByConsumer(User user, Integer page) {
        return orderRepository.findByConsumer(user.getId(), page*5)
                .stream()
                .map(result -> {
                    Long id = (Long) result[0];
                    Date date = (Date) result[1];
                    Integer stId = (Integer) result[2];
                    String stName = (String) result[3];
                    String stDescription = (String) result[4];

                    OrderStatus orderStatus = new OrderStatus();
                    orderStatus.setId(stId);
                    orderStatus.setName(stName);
                    orderStatus.setDescription(stDescription);

                    Integer amount = ((Long) result[5]).intValue();
                    Integer totalSum = ((Long) result[6]).intValue();

                    return new OrderDto(id, date, orderStatus, amount, totalSum);
                })
                .toList();
    }

    @Transactional
    public OrderDto createOrder(User user, List<OrderProductPlacementDto> productDtos) {
        Order order = new Order();

        List<Long> productsIds = productDtos.stream()
                .map(OrderProductPlacementDto::getProductId)
                .toList();

        List<Product> products = productRepository.getAllByIdIn(productsIds);

        int totalSum = products.stream().map(Product::getPrice).reduce(0, Integer::sum);
        int totalCount = products.size();

        List<OrderProduct> orderProducts = IntStream.range(0, products.size())
                .mapToObj(i -> {
                    Product product = products.get(i);
                    OrderProductPlacementDto opDto = productDtos.get(i);
                    OrderProduct orderProduct = new OrderProduct();

                    OrderProductId orderProductPk = new OrderProductId();
                    orderProductPk.setProduct(product);
                    orderProductPk.setOrder(order);

                    orderProduct.setPk(orderProductPk);
                    orderProduct.setQuantity(opDto.getQuantity());
                    orderProduct.setPrice(product.getPrice());
                    return orderProduct;
                }).toList();


        Date now = new Date();
        order.setDate(now);

        OrderStatus orderStatus = orderStatusService.getOrderStatusByName("UNPAID");
        order.setStatus(orderStatus);
        order.setConsumer(user);
        order.setProducts(orderProducts);

        Order savedOrder = orderRepository.save(order);

        orderProductRepository.saveAll(orderProducts);

        return new OrderDto(savedOrder.getId(), now, orderStatus, totalSum, totalCount);
    }

}
