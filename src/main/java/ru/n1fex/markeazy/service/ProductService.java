package ru.n1fex.markeazy.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.n1fex.markeazy.entity.Product;
import ru.n1fex.markeazy.entity.ProductCard;
import ru.n1fex.markeazy.repository.ProductRepository;
import ru.n1fex.markeazy.util.Compressor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository repository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.repository = productRepository;
    }

    private void addImageToProductCard(ProductCard product) {
        File folder = new File("D:\\SavedImages\\"+product.getId());
        File[] files = folder.listFiles();
        if (files != null && files.length > 0) {
            File imageFile = files[new Random().nextInt(files.length)];
            if (imageFile.exists()) {
//                try {
//                    product.setImage(Compressor.getCompressedImageAsByteArray(imageFile.getAbsolutePath()));
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
            }
        }
    }

    public List<ProductCard> getRandomProducts(int limit) {
        if (limit <= 0) {
            limit = 10;
        }
        List<ProductCard> products = repository.getRandomProductsLimited(Math.min(limit, 50));
        products.forEach(this::addImageToProductCard);
        return products;
    }

    public Optional<Product> getProductById(Long id) {
        return repository.findById(id);
    }

    public List<ProductCard> findProductByTitleOffsetLimit(String title, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);

        long startTime = System.nanoTime();
        List<Product> products = repository.findByTitleContainingIgnoreCase(title, pageable);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        log.info("Founded for: {} ms", duration / 1000000);
        return products
                .stream()
                .map(product -> {
                    ProductCard card = new ProductCard(product);
                    addImageToProductCard(card);
                    return card;
                })
                .collect(Collectors.toList());
    }
}
