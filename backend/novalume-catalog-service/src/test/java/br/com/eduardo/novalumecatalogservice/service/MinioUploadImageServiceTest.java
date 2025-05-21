package br.com.eduardo.novalumecatalogservice.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link MinioUploadImageService}
 */
@ExtendWith(MockitoExtension.class)
class MinioUploadImageServiceTest {

    @Mock
    private MinioClient minioClient;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private MinioUploadImageService minioUploadImageService;

    @Captor
    private ArgumentCaptor<PutObjectArgs> putObjectArgsCaptor;

    private static final String BUCKET_NAME = "test-bucket";
    private static final String BASE_URL = "http://localhost:9000";
    private static final String IMAGE_CONTENT_TYPE = "image/jpeg";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(minioUploadImageService, "BUCKET_NAME", BUCKET_NAME);
        ReflectionTestUtils.setField(minioUploadImageService, "BASE_URL", BASE_URL);
    }

    @Test
    @DisplayName("Should successfully upload image when bucket exists")
    void shouldSuccessfullyUploadImageWhenBucketExists() throws Exception {
        // Arrange
        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(true);
        when(multipartFile.getOriginalFilename()).thenReturn("test-image.jpg");
        when(multipartFile.getContentType()).thenReturn(IMAGE_CONTENT_TYPE);
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("test data".getBytes()));
        when(multipartFile.getSize()).thenReturn(9L);

        // Act
        String result = minioUploadImageService.uploadImageToMinio(multipartFile);

        // Assert
        verify(minioClient).putObject(putObjectArgsCaptor.capture());
        verify(minioClient, never()).makeBucket(any(MakeBucketArgs.class));

        PutObjectArgs capturedArgs = putObjectArgsCaptor.getValue();
        assertEquals(BUCKET_NAME, capturedArgs.bucket());
        assertEquals(IMAGE_CONTENT_TYPE, capturedArgs.contentType());

        assertThat(result).startsWith(BASE_URL + "/" + BUCKET_NAME + "/");
        assertThat(result).endsWith(".jpg");
    }

    @Test
    @DisplayName("Should create bucket and upload image when bucket does not exist")
    void shouldCreateBucketAndUploadImageWhenBucketDoesNotExist() throws Exception {
        // Arrange
        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn("test-image.png");
        when(multipartFile.getContentType()).thenReturn("image/png");
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("test data".getBytes()));
        when(multipartFile.getSize()).thenReturn(9L);

        // Act
        String result = minioUploadImageService.uploadImageToMinio(multipartFile);

        // Assert
        verify(minioClient).makeBucket(any(MakeBucketArgs.class));
        verify(minioClient).putObject(any(PutObjectArgs.class));

        assertThat(result).startsWith(BASE_URL + "/" + BUCKET_NAME + "/");
        assertThat(result).endsWith(".png");
    }

    @Test
    @DisplayName("Should handle file with null filename")
    void shouldHandleFileWithNullFilename() throws Exception {
        // Arrange
        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(true);
        when(multipartFile.getOriginalFilename()).thenReturn(null);
        when(multipartFile.getContentType()).thenReturn("application/octet-stream");
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("test data".getBytes()));
        when(multipartFile.getSize()).thenReturn(9L);

        // Act
        String result = minioUploadImageService.uploadImageToMinio(multipartFile);

        // Assert
        verify(minioClient).putObject(any(PutObjectArgs.class));
        assertThat(result).startsWith(BASE_URL + "/" + BUCKET_NAME + "/");
        assertThat(result).doesNotContain(".");
    }

    @Test
    @DisplayName("Should throw exception when bucket verification fails")
    void shouldThrowExceptionWhenBucketVerificationFails() throws Exception {
        // Arrange
        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenThrow(new RuntimeException("Bucket verification failed"));

        // Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> minioUploadImageService.uploadImageToMinio(multipartFile));
        assertThat(exception).hasMessageContaining("Bucket verification failed");
    }

    @Test
    @DisplayName("Should throw exception when bucket creation fails")
    void shouldThrowExceptionWhenBucketCreationFails() throws Exception {
        // Arrange
        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(false);
        doThrow(new RuntimeException("Bucket creation failed"))
                .when(minioClient).makeBucket(any(MakeBucketArgs.class));

        // Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> minioUploadImageService.uploadImageToMinio(multipartFile));
        assertThat(exception).hasMessageContaining("Bucket creation failed");
    }

    @Test
    @DisplayName("Should throw exception when file upload fails")
    void shouldThrowExceptionWhenFileUploadFails() throws Exception {
        // Arrange
        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(true);
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
        when(multipartFile.getContentType()).thenReturn(IMAGE_CONTENT_TYPE);
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("test data".getBytes()));
        when(multipartFile.getSize()).thenReturn(9L);
        doThrow(new RuntimeException("Upload failed"))
                .when(minioClient).putObject(any(PutObjectArgs.class));

        // Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> minioUploadImageService.uploadImageToMinio(multipartFile));
        assertThat(exception).hasMessageContaining("Upload error");
    }
}