package ru.n1fex.markeazy.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.n1fex.markeazy.entity.Product;
import ru.n1fex.markeazy.entity.ProductDoc;
import ru.n1fex.markeazy.repository.ProductElasticsearchRepository;
import ru.n1fex.markeazy.repository.ProductRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductIndexingService {

    private final ProductRepository productRepository;
    private final ProductElasticsearchRepository productElasticsearchRepository;

    @Transactional(readOnly = true)
    public void reindexAllProducts() {
        List<Product> products = productRepository.findAll();

        productElasticsearchRepository.saveAll(
                products.stream().map(
                        product -> new ProductDoc(
                                product.getId(),
                                product.getTitle(),
                                product.getDescription(),
                                product.getPrice()
                        )
                ).toList()
        );
    }

}
