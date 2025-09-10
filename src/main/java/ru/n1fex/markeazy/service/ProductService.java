package ru.n1fex.markeazy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.n1fex.markeazy.dto.ProductCardDto;
import ru.n1fex.markeazy.entity.Product;
import ru.n1fex.markeazy.mapper.ProductMapper;
import ru.n1fex.markeazy.repository.ProductRepository;
import ru.n1fex.markeazy.util.Compressor;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    private void addImageToProductCard(ProductCardDto product) {
        File folder = new File("D:\\SavedImages\\"+product.getId());
        File[] files = folder.listFiles();
        if (files != null && files.length > 0) {
            File imageFile = files[new Random().nextInt(files.length)];
            if (imageFile.exists()) {
                try {
                    product.setImage(Compressor.getCompressedImageAsByteArray(imageFile.getAbsolutePath()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public List<ProductCardDto> getRandomProducts(int limit) {
        if (limit <= 0) {
            limit = 10;
        }
        List<ProductCardDto> products = repository
                .getRandomProductsLimited(Math.min(limit, 50))
                .stream().map(mapper::toProductCardDto)
                .toList();
        products.forEach(this::addImageToProductCard);
        return products;
    }

    public Optional<Product> getProductById(Long id) {
        return repository.findById(id);
    }

    public List<ProductCardDto> findProductByTitleOffsetLimit(String title, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);

        List<Product> products = repository.findByTitleContainingIgnoreCase(title, pageable);

        return products.stream().map(mapper::toProductCardDto).toList();
    }
}
