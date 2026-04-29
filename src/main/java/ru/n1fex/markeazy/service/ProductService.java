package ru.n1fex.markeazy.service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.n1fex.markeazy.dto.ProductCardDto;
import ru.n1fex.markeazy.entity.Product;
import ru.n1fex.markeazy.entity.ProductDoc;
import ru.n1fex.markeazy.entity.Seller;
import ru.n1fex.markeazy.mapper.ProductMapper;
import ru.n1fex.markeazy.repository.ProductElasticsearchRepository;
import ru.n1fex.markeazy.repository.ProductRepository;

import java.util.*;

import static java.util.Comparator.comparingInt;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    @Value("${app.minio.bucket.product-image}")
    private String PRODUCT_IMAGE_BUCKET_NAME;

    private final MinioClient minioClient;

    private final ProductRepository productRepository;
    private final ProductIndexingService productIndexingService;

    private final ProductElasticsearchRepository elasticsearchRepository;
    private final ProductMapper mapper;

    private String getImageNameInBucket(Seller seller, MultipartFile image) {
        UUID uuid = UUID.randomUUID();

        String origName = image.getOriginalFilename();
        String ext = origName.substring(origName.lastIndexOf('.') + 1);

        return seller.getId() + "/" + uuid.toString() + "." + ext;
    }

    public void deleteProduct(Seller seller, Product product) {
        product.setDeleted(true);
        productRepository.save(product);
    }

    public Product changeProduct(Seller seller,
                              Product product,
                              String title,
                              String description,
                              Integer amount,
                              Integer price,
                              Integer discount,
                              MultipartFile image) throws Exception {
        if (image != null) {
            String objectKey = getImageNameInBucket(seller, image);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(PRODUCT_IMAGE_BUCKET_NAME)
                            .object(objectKey)
                            .stream(image.getInputStream(), image.getSize(), -1)
                            .contentType(image.getContentType())
                            .build()
            );

            product.setObjectKey(objectKey);
        }

        if (title != null) {
            product.setTitle(title);
        }
        if (description != null) {
            product.setDescription(description);
        }
        if (price != null) {
            product.setPrice(price);
        }
        if (discount != null) {
            product.setDiscount(discount);
        }
        if (amount != null) {
            product.setAmount(amount);
        }

        Product savedProduct = productRepository.save(product);
        savedProduct.setObjectKey(getPresignedUrl(savedProduct));
        productIndexingService.reindexAllProducts();
        return savedProduct;
    }

    public Product addProduct(Seller seller,
                              String title,
                              String description,
                              Integer amount,
                              Integer price,
                              Integer discount,
                              MultipartFile image)
            throws Exception {

        String objectKey = getImageNameInBucket(seller, image);

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(PRODUCT_IMAGE_BUCKET_NAME)
                        .object(objectKey)
                        .stream(image.getInputStream(), image.getSize(), -1)
                        .contentType(image.getContentType())
                        .build()
        );

        Product product = new Product();
        product.setTitle(title);
        product.setDescription(description);
        product.setAmount(amount);
        product.setSeller(seller);
        product.setPrice(price);
        product.setDiscount(discount);
        product.setRating(0);
        product.setReviewsCount(0);
        product.setObjectKey(objectKey);

        Product savedProduct = productRepository.save(product);
        savedProduct.setObjectKey(getPresignedUrl(savedProduct));
        productIndexingService.reindexAllProducts();
        return savedProduct;
    }

    public List<ProductCardDto> getRandomProducts(int limit) {
        if (limit <= 0) {
            limit = 10;
        }
        return productRepository
                .getRandomProductsLimited(Math.min(limit, 50))
                .stream().map(product -> {
                    product.setObjectKey(getPresignedUrl(product));
                    return mapper.toProductCardDto(product);
                })
                .toList();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<ProductCardDto> findProductsViaElastic(String searchText, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset/limit, limit);
        List<ProductDoc> foundDocs = elasticsearchRepository.searchByQuery(searchText, pageable);

        Map<Long, Integer> idsMap = new HashMap<>();
        for (int i = 0; i < foundDocs.size(); i++) {
            idsMap.put(foundDocs.get(i).getId(), i);
        }

        Set<Long> ids = idsMap.keySet();

        List<Product> products = productRepository.getAllByIdIn(ids.stream().toList());
        products.sort(comparingInt(prod -> idsMap.get(prod.getId())));

        return products.stream().map(p -> {
            p.setObjectKey(getPresignedUrl(p));
            return mapper.toProductCardDto(p);
        }).toList();
    }

    public List<ProductCardDto> getAllByIdIn(List<Long> ids) {
        List<Product> products = productRepository.getAllByIdIn(ids);
        return products.stream().map(product -> {
            product.setObjectKey(getPresignedUrl(product));
            return mapper.toProductCardDto(product);
        }).toList();
    }

    public String getPresignedUrl(Product product) {
        if (product.getObjectKey() == null) {
            return "";
        }
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(PRODUCT_IMAGE_BUCKET_NAME)
                            .object(product.getObjectKey())
                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
