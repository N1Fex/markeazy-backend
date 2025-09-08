package ru.n1fex.markeazy.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.n1fex.markeazy.MarkeazyApplication;
import ru.n1fex.markeazy.dto.ProductCardDto;
import ru.n1fex.markeazy.entity.Product;
import ru.n1fex.markeazy.entity.Seller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MarkeazyApplication.class)
public class ProductMapperTests {

    ProductMapper mapper = new ProductMapperImpl();

    @Test
    public void testProductToProductCard() {
        Seller seller = new Seller();
        seller.setId(2L);
        seller.setName("Samsung");

        Product product = new Product();
        product.setId(14L);
        product.setTitle("Samsung Galaxy 20S");
        product.setPrice(60000);
        product.setDiscount(12);
        product.setAmount(20);
        product.setRating(4.7f);
        product.setReviewsCount(456);
        product.setSeller(seller);

        ProductCardDto card = mapper.toProductCardDto(product);

        assertNotNull(card);

        assertAll(
                () -> assertEquals(product.getId(), card.getId()),
                () -> assertEquals(product.getTitle(), card.getTitle()),
                () -> assertEquals(product.getPrice(), card.getPrice()),
                () -> assertEquals(product.getDiscount(), card.getDiscount()),
                () -> assertEquals(product.getAmount(), card.getAmount()),
                () -> assertEquals(product.getRating(), card.getRating()),
                () -> assertEquals(product.getReviewsCount(), card.getReviewsCount()),
                () -> assertEquals(product.getSeller().getName(), card.getSeller())
        );

    }
}
