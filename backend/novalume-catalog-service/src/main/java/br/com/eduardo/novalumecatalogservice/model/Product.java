package br.com.eduardo.novalumecatalogservice.model;

import br.com.eduardo.novalumecatalogservice.model.enums.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {
    private String id;
    private String productName;
    private String productDescription;
    private double unitPrice;
    private boolean sellIndicator;
    private boolean deleted;
    private ProductCategory productCategory;
    private byte productRating;
    private List<String> imagesUrl = new ArrayList<>();
    private boolean featured;
}
