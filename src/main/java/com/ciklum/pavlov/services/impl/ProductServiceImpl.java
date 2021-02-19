package com.ciklum.pavlov.services.impl;

import com.ciklum.pavlov.dao.ProductDao;
import com.ciklum.pavlov.jdbc.TransactionManager;
import com.ciklum.pavlov.models.Product;
import com.ciklum.pavlov.services.ProductService;

import java.util.List;
import java.util.Optional;

public class ProductServiceImpl implements ProductService {
    private final TransactionManager transactionManager;
    private final ProductDao productDao;

    public ProductServiceImpl(TransactionManager transactionManager, ProductDao productDao) {
        this.transactionManager = transactionManager;
        this.productDao = productDao;
    }

    @Override
    public int addProduct(Product product) {
        return transactionManager.executeInTransaction(() -> productDao.addProduct(product));
    }

    @Override
    public List<Product> getAllOrderedProducts() {
        return transactionManager.executeInTransaction(productDao::extractAllOrderedProducts);
    }

    @Override
    public List<Product> getAllProducts() {
        return transactionManager.executeInTransaction(productDao::extractAllProducts);
    }

    @Override
    public boolean removeProduct(int productId) {
        int updatedRows = transactionManager.executeInTransaction(() -> productDao.removeProduct(productId));
        return updatedRows > 0;
    }

    @Override
    public int removeAllProducts() {
        return transactionManager.executeInTransaction(productDao::removeAllProducts);
    }

    @Override
    public Optional<Product> getProduct(long productIdentifier) {
        return transactionManager.executeInTransaction(() -> productDao.getProductById(productIdentifier));
    }
}
