package br.com.eduardo.novalumecatalogservice.service;

import br.com.eduardo.novalumecatalogservice.dto.ProductDto;
import br.com.eduardo.novalumecatalogservice.infra.exception.custom.EntityAlreadyExistsException;
import br.com.eduardo.novalumecatalogservice.infra.exception.custom.EntityNotFoundException;
import br.com.eduardo.novalumecatalogservice.mapper.ProductMapper;
import br.com.eduardo.novalumecatalogservice.model.Product;
import br.com.eduardo.novalumecatalogservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper mapper;
    private final MinioUploadImageService imageService;

    public Product createProduct(ProductDto productDto) {

        if (productRepository.getProductByProductName(productDto.productName()).isPresent()) {
            throw new EntityAlreadyExistsException("already Exists");
        }

        return productRepository.save(mapper.mapProductDtoToProductEntity(productDto));
    }

    public Product findProductById(String productId) {
        return productRepository.getProductById(productId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Product %s not found", productId)));
    }

    public void uploadImage(String productId, MultipartFile file) {

        Optional<Product> product = productRepository.getProductById(productId);

        if (product.isEmpty()) {
            throw new EntityNotFoundException(String.format("Product %s not found", productId));
        }

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null");
        }

        String imageUrl = imageService.uploadImageToMinio(file);

        Product existingProduct = product.get();

        existingProduct.getImagesUrl().add(imageUrl);

        productRepository.save(existingProduct);
    }
}
