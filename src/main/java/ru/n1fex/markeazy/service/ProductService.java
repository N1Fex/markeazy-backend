package ru.n1fex.markeazy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.n1fex.markeazy.dto.ProductCardDto;
import ru.n1fex.markeazy.entity.Product;
import ru.n1fex.markeazy.entity.ProductDoc;
import ru.n1fex.markeazy.mapper.ProductMapper;
import ru.n1fex.markeazy.repository.ProductElasticsearchRepository;
import ru.n1fex.markeazy.repository.ProductRepository;

import java.util.*;

import static java.util.Comparator.comparingInt;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository repository;
    private final ProductElasticsearchRepository elasticsearchRepository;
    private final ProductMapper mapper;


    public List<ProductCardDto> getRandomProducts(int limit) {
        if (limit <= 0) {
            limit = 10;
        }
        return repository
                .getRandomProductsLimited(Math.min(limit, 50))
                .stream().map(mapper::toProductCardDto)
                .toList();
    }

    public Optional<Product> getProductById(Long id) {
        return repository.findById(id);
    }

    public List<ProductCardDto> findProductByTitleOffsetLimit(String title, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);

        List<Product> products = repository.findByTitleContainingIgnoreCase(title, pageable);

        return products.stream().map(mapper::toProductCardDto).toList();
    }

    public List<ProductCardDto> findProductsViaElastic(String searchText, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset/limit, limit);
        List<ProductDoc> foundDocs = elasticsearchRepository.searchByQuery(searchText, pageable);

        Map<Long, Integer> idsMap = new HashMap<>();
        for (int i = 0; i < foundDocs.size(); i++) {
            idsMap.put(foundDocs.get(i).getId(), i);
        }

        Set<Long> ids = idsMap.keySet();

        List<Product> products = repository.getAllByIdIn(ids.stream().toList());
        products.sort(comparingInt(prod -> idsMap.get(prod.getId())));

        return products.stream().map(mapper::toProductCardDto).toList();
    }

    public List<ProductCardDto> getAllByIdIn(List<Long> ids) {
        List<Product> products = repository.getAllByIdIn(ids);
        return products.stream().map(mapper::toProductCardDto).toList();
    }

}
