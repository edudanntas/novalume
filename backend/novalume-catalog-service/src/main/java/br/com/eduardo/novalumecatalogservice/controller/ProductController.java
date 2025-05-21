package br.com.eduardo.novalumecatalogservice.controller;

import br.com.eduardo.novalumecatalogservice.dto.ProductCreateDTO;
import br.com.eduardo.novalumecatalogservice.dto.ProductResponseListDto;
import br.com.eduardo.novalumecatalogservice.dto.ProductUpdateDto;
import br.com.eduardo.novalumecatalogservice.model.Product;
import br.com.eduardo.novalumecatalogservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductCreateDTO productCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productCreateDTO));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseListDto>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping(path = "/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable String productId) {
        return ResponseEntity.ok(productService.findProductById(productId));
    }

    @PutMapping(path = "/{productId}")
    public ResponseEntity<Void> uploadProductImage(@PathVariable String productId, @RequestPart("image") MultipartFile file) {
        productService.uploadImage(productId, file);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id, ProductUpdateDto productUpdateDto) {
        return ResponseEntity.ok(productService.updateProduct(id, productUpdateDto));
    }

    @DeleteMapping(path = "/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
