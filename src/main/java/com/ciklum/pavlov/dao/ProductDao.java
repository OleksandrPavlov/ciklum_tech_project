package com.ciklum.pavlov.dao;

import com.ciklum.pavlov.models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDao {
    int addProduct(Product product);

    List<Product> extractAllOrderedProducts();

    List<Product> extractAllProducts();

    int removeProduct(int productId);

    int removeAllProducts();

    Optional<Product> getProductById(long productId);
}
