package br.com.eduardo.novalumecatalogservice.service;

import br.com.eduardo.novalumecatalogservice.dto.ProductCreateDTO;
import br.com.eduardo.novalumecatalogservice.dto.ProductUpdateDto;
import br.com.eduardo.novalumecatalogservice.infra.exception.custom.EntityAlreadyExistsException;
import br.com.eduardo.novalumecatalogservice.infra.exception.custom.EntityNotFoundException;
import br.com.eduardo.novalumecatalogservice.mapper.ProductMapper;
import br.com.eduardo.novalumecatalogservice.model.Product;
import br.com.eduardo.novalumecatalogservice.model.enums.ProductCategory;
import br.com.eduardo.novalumecatalogservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper mapper;

    @Mock
    private MinioUploadImageService imageService;

    @InjectMocks
    private ProductService productService;

    private static final String PRODUCT_ID = "product123";
    private static final String PRODUCT_NAME = "Smartphone";
    private static final String PRODUCT_DESCRIPTION = "A great smartphone";
    private static final double PRODUCT_PRICE = 999.99;
    private static final boolean SELL_INDICATOR = true;
    private static final ProductCategory CATEGORY = ProductCategory.ELECTRONICS;
    private static final List<String> IMAGE_URLS = List.of("http://example.com/image1.jpg");
    private static final boolean FEATURED = true;
    private static final String UPDATED_SUFFIX = " updated";
    private static final String NOT_FOUND_MESSAGE = "Product %s not found";
    private static final String ALREADY_EXISTS_MESSAGE = "already Exists";
    private static final String EMPTY_FILE_MESSAGE = "File cannot be null";

    private Product product;
    private ProductCreateDTO productCreateDTO;
    private ProductUpdateDto productUpdateDto;
    private MultipartFile mockFile;

    @BeforeEach
    void setUp() {
        // Setup standard product
        product = new Product();
        product.setId(PRODUCT_ID);
        product.setProductName(PRODUCT_NAME);
        product.setProductDescription(PRODUCT_DESCRIPTION);
        product.setUnitPrice(PRODUCT_PRICE);
        product.setSellIndicator(SELL_INDICATOR);
        product.setProductCategory(CATEGORY);
        product.setImagesUrl(new ArrayList<>(IMAGE_URLS));
        product.setFeatured(FEATURED);

        // Setup standard DTO
        productCreateDTO = new ProductCreateDTO(
                PRODUCT_NAME,
                PRODUCT_DESCRIPTION,
                PRODUCT_PRICE,
                CATEGORY,
                FEATURED
        );

        // Setup update DTO with optional fields
        productUpdateDto = new ProductUpdateDto(
                Optional.of(PRODUCT_NAME + UPDATED_SUFFIX),
                Optional.of(PRODUCT_DESCRIPTION + UPDATED_SUFFIX),
                Optional.of(PRODUCT_PRICE + 10.00),
                Optional.of(!SELL_INDICATOR),
                Optional.of(CATEGORY),
                Optional.of(List.of("http://example.com/image2.jpg")),
                Optional.of(!FEATURED)
        );

        mockFile = mock(MultipartFile.class);
    }

    @Test
    @DisplayName("Should create product successfully when product name doesn't exist")
    void shouldCreateProductSuccessfullyWhenProductNameDoesntExist() {
        when(productRepository.getProductByProductName(PRODUCT_NAME)).thenReturn(Optional.empty());
        when(mapper.mapCreateProductDtoToProductEntity(productCreateDTO)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.createProduct(productCreateDTO);

        assertNotNull(result);
        assertEquals(PRODUCT_ID, result.getId());
        assertEquals(PRODUCT_NAME, result.getProductName());

        verify(productRepository).getProductByProductName(PRODUCT_NAME);
        verify(mapper).mapCreateProductDtoToProductEntity(productCreateDTO);
        verify(productRepository).save(product);
    }

    @Test
    @DisplayName("Should throw exception when creating product with existing name")
    void shouldThrowExceptionWhenCreatingProductWithExistingName() {
        when(productRepository.getProductByProductName(PRODUCT_NAME)).thenReturn(Optional.of(product));

        EntityAlreadyExistsException exception = assertThrows(EntityAlreadyExistsException.class,
                () -> productService.createProduct(productCreateDTO));

        assertEquals(ALREADY_EXISTS_MESSAGE, exception.getMessage());

        verify(productRepository).getProductByProductName(PRODUCT_NAME);
        verify(mapper, never()).mapCreateProductDtoToProductEntity(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should find product by ID successfully when product exists")
    void shouldFindProductByIdSuccessfullyWhenProductExists() {
        when(productRepository.getProductById(PRODUCT_ID)).thenReturn(Optional.of(product));

        Product result = productService.findProductById(PRODUCT_ID);

        assertNotNull(result);
        assertEquals(PRODUCT_ID, result.getId());

        verify(productRepository).getProductById(PRODUCT_ID);
    }

    @Test
    @DisplayName("Should throw exception when product not found by ID")
    void shouldThrowExceptionWhenProductNotFoundById() {
        when(productRepository.getProductById(PRODUCT_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> productService.findProductById(PRODUCT_ID));

        assertEquals(String.format(NOT_FOUND_MESSAGE, PRODUCT_ID), exception.getMessage());

        verify(productRepository).getProductById(PRODUCT_ID);
    }

    @Test
    @DisplayName("Should update product successfully when product exists")
    void shouldUpdateProductSuccessfullyWhenProductExists() {
        when(productRepository.getProductById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.updateProduct(PRODUCT_ID, productUpdateDto);

        assertNotNull(result);
        assertEquals(PRODUCT_NAME + UPDATED_SUFFIX, product.getProductName());
        assertEquals(PRODUCT_DESCRIPTION + UPDATED_SUFFIX, product.getProductDescription());
        assertEquals(PRODUCT_PRICE + 10.00, product.getUnitPrice());
        assertEquals(!SELL_INDICATOR, product.isSellIndicator());
        assertFalse(product.getImagesUrl().isEmpty());
        assertEquals(!FEATURED, product.isFeatured());

        verify(productRepository).getProductById(PRODUCT_ID);
        verify(productRepository).save(product);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent product")
    void shouldThrowExceptionWhenUpdatingNonExistentProduct() {
        when(productRepository.getProductById(PRODUCT_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> productService.updateProduct(PRODUCT_ID, productUpdateDto));

        assertEquals(String.format(NOT_FOUND_MESSAGE, PRODUCT_ID), exception.getMessage());

        verify(productRepository).getProductById(PRODUCT_ID);
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete product successfully when product exists")
    void shouldDeleteProductSuccessfullyWhenProductExists() {
        when(productRepository.getProductById(PRODUCT_ID)).thenReturn(Optional.of(product));

        productService.deleteProduct(PRODUCT_ID);

        verify(productRepository).getProductById(PRODUCT_ID);
        verify(productRepository).delete(product);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent product")
    void shouldThrowExceptionWhenDeletingNonExistentProduct() {
        when(productRepository.getProductById(PRODUCT_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> productService.deleteProduct(PRODUCT_ID));

        assertEquals(String.format(NOT_FOUND_MESSAGE, PRODUCT_ID), exception.getMessage());

        verify(productRepository).getProductById(PRODUCT_ID);
        verify(productRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should upload image successfully when product exists and file is valid")
    void shouldUploadImageSuccessfullyWhenProductExistsAndFileIsValid() {
        when(productRepository.getProductById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(mockFile.isEmpty()).thenReturn(false);

        String newImageUrl = "http://example.com/newimage.jpg";
        when(imageService.uploadImageToMinio(mockFile)).thenReturn(newImageUrl);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.uploadImage(PRODUCT_ID, mockFile);

        assertTrue(product.getImagesUrl().contains(newImageUrl));

        verify(productRepository).getProductById(PRODUCT_ID);
        verify(mockFile).isEmpty();
        verify(imageService).uploadImageToMinio(mockFile);
        verify(productRepository).save(product);
    }

    @Test
    @DisplayName("Should throw exception when uploading image to non-existent product")
    void shouldThrowExceptionWhenUploadingImageToNonExistentProduct() {
        when(productRepository.getProductById(PRODUCT_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> productService.uploadImage(PRODUCT_ID, mockFile));

        assertEquals(String.format(NOT_FOUND_MESSAGE, PRODUCT_ID), exception.getMessage());

        verify(productRepository).getProductById(PRODUCT_ID);
        verify(mockFile, never()).isEmpty();
        verify(imageService, never()).uploadImageToMinio(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when uploading empty file")
    void shouldThrowExceptionWhenUploadingEmptyFile() {
        when(productRepository.getProductById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(mockFile.isEmpty()).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> productService.uploadImage(PRODUCT_ID, mockFile));

        assertEquals(EMPTY_FILE_MESSAGE, exception.getMessage());

        verify(productRepository).getProductById(PRODUCT_ID);
        verify(mockFile).isEmpty();
        verify(imageService, never()).uploadImageToMinio(any());
        verify(productRepository, never()).save(any());
    }
}