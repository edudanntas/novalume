package br.com.eduardo.novalumecatalogservice.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioS3Config {

    @Value("${minio.access-key}")
    private String ACCESS_KEY;

    @Value("${minio.secret-key}")
    private String SECRET_KEY ;

    @Value("${minio.base-url}")
    private String BASE_URL ;

    @Bean
    MinioClient minioClient() {
        return MinioClient.builder()
                .credentials(ACCESS_KEY, SECRET_KEY)
                .endpoint(BASE_URL)
                .build();
    }
}
