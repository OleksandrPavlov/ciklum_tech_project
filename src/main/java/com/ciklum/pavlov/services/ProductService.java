package com.ciklum.pavlov.services;

import com.ciklum.pavlov.models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    int addProduct(Product product);

    List<Product> getAllOrderedProducts();

    List<Product> getAllProducts();

    boolean removeProduct(int productId);

    int removeAllProducts();

    Optional<Product> getProduct(long productIdentifier);
}
