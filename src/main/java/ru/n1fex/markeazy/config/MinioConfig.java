package ru.n1fex.markeazy.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${app.minio.access-key}")
    private String accessKey;
    @Value("${app.minio.secret-key}")
    private String secretKey;
    @Value("${app.minio.url}")
    private String url;
    @Value("${app.minio.bucket.product-image}")
    private String PRODUCT_IMAGE_BUCKET_NAME;

    @SneakyThrows
    @Bean
    public MinioClient minioClient() {
        MinioClient client = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();

        boolean found = client.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(PRODUCT_IMAGE_BUCKET_NAME)
                        .build()
        );

        if (!found) {
            client.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(PRODUCT_IMAGE_BUCKET_NAME)
                            .build()
            );
        }

        return client;
    }

}
