package com.ciklum.pavlov.dao.impl;

import com.ciklum.pavlov.constants.SQLQueriesConstants;
import com.ciklum.pavlov.dao.ProductDao;
import com.ciklum.pavlov.jdbc.handler.HandlerFactory;
import com.ciklum.pavlov.models.Product;
import com.ciklum.pavlov.util.ThreadLocalConnection;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static com.ciklum.pavlov.constants.SQLQueriesConstants.*;
import static com.ciklum.pavlov.jdbc.handler.JDBCUtil.putErrorMsgToConnection;

public class ProductDaoImpl implements ProductDao {
    private final Properties sqlProperties;
    private final QueryRunner queryRunner;

    public ProductDaoImpl(Properties sqlProperties) {
        this.sqlProperties = sqlProperties;
        queryRunner = new QueryRunner();
    }

    @Override

    public int addProduct(Product product) {
        Connection connection = ThreadLocalConnection.getConnection();
        try {
            return queryRunner.update(connection, sqlProperties.getProperty(ADD_PRODUCT),
                    product.getName(),
                    product.getPrice(),
                    product.getProductStatus().toString());
        } catch (SQLException ex) {
            putErrorMsgToConnection(ex.getMessage(), connection);
        }
        return 0;
    }

    @Override
    public List<Product> extractAllOrderedProducts() {
        return getProductList(sqlProperties.getProperty(GET_ALL_ORDERED_PRODUCTS));
    }

    @Override
    public List<Product> extractAllProducts() {
        return getProductList(sqlProperties.getProperty(GET_ALL_PRODUCTS));
    }

    private List<Product> getProductList(String sqlQuery) {
        Connection connection = ThreadLocalConnection.getConnection();
        try {
            return queryRunner.query(connection, sqlQuery, HandlerFactory.PRODUCT_LIST_HANDLER);
        } catch (SQLException ex) {
            putErrorMsgToConnection(ex.getMessage(), connection);
        }
        return Collections.emptyList();
    }

    @Override
    public int removeProduct(int productId) {
        return update(sqlProperties.getProperty(SQLQueriesConstants.REMOVE_PRODUCT_BY_ID),productId);
    }

    @Override
    public int removeAllProducts() {
        return update(sqlProperties.getProperty(SQLQueriesConstants.DELETE_ALL_PRODUCTS));
    }

    private int update(String query,Object...parameters) {
        Connection connection = ThreadLocalConnection.getConnection();
        try {
            return queryRunner.update(connection, query,parameters);
        } catch (SQLException ex) {
            putErrorMsgToConnection(ex.getMessage(), connection);
        }
        return 0;
    }

    @Override
    public Optional<Product> getProductById(long productId) {
        Connection connection = ThreadLocalConnection.getConnection();
        try {
            return queryRunner.query(connection, sqlProperties.getProperty(SQLQueriesConstants.GET_PRODUCT_BY_ID), HandlerFactory.PRODUCT_RESULT_SET_HANDLER, productId);
        } catch (SQLException ex) {
            putErrorMsgToConnection(ex.getMessage(), connection);
        }
        return Optional.empty();
    }
}
