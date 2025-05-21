package br.com.eduardo.novalumecatalogservice.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioUploadImageService {
    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String BUCKET_NAME;

    @Value("${minio.base-url}")
    private String BASE_URL;

    public String uploadImageToMinio(MultipartFile file) {
        verifyBucketCreation();

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            String fileName = UUID.randomUUID() + extension;

            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .contentType(file.getContentType())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .object(fileName)
                    .build());

            return String.format("%s/%s/%s", BASE_URL, BUCKET_NAME, fileName);
        } catch (Exception e) {
            throw new RuntimeException("Upload error: " + e.getMessage(), e);
        }
    }

    private void verifyBucketCreation() {
        try {
            boolean foundBucket = minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build());

            if (!foundBucket) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
