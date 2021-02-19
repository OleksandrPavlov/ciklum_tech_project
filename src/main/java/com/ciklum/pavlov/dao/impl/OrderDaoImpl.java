package com.ciklum.pavlov.dao.impl;

import com.ciklum.pavlov.dao.OrderDao;
import com.ciklum.pavlov.models.Order;
import com.ciklum.pavlov.util.ThreadLocalConnection;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

import static com.ciklum.pavlov.constants.SQLQueriesConstants.*;
import static com.ciklum.pavlov.jdbc.handler.HandlerFactory.ORDER_HANDLER;
import static com.ciklum.pavlov.jdbc.handler.JDBCUtil.putErrorMsgToConnection;

public class OrderDaoImpl implements OrderDao {
    private final Properties sqlProperties;
    private final QueryRunner queryRunner;

    public OrderDaoImpl(Properties sqlProperties) {
        this.sqlProperties = sqlProperties;
        queryRunner = new QueryRunner();
    }

    @Override
    public int updateOrderItemQuantity(long productId, long orderId, int quantity) {
        Connection connection = ThreadLocalConnection.getConnection();
        try {
            return queryRunner.update(connection, sqlProperties.getProperty(UPDATE_ORDER_PRODUCT_QUANTITY), quantity, productId, orderId);
        } catch (SQLException ex) {
            putErrorMsgToConnection(ex.getMessage(), connection);
        }
        return 0;
    }

    @Override
    public Optional<Order> getOrder(long userId) {
        Connection connection = ThreadLocalConnection.getConnection();
        try {
            return queryRunner.query(connection, sqlProperties.getProperty(GET_ORDER_BY_USER_ID), ORDER_HANDLER, userId);
        } catch (SQLException ex) {
            putErrorMsgToConnection(ex.getMessage(), connection);
        }
        return Optional.empty();
    }

    @Override
    public int addOrderItem(Order.OrderItem orderItem, long orderId) {
        Connection connection = ThreadLocalConnection.getConnection();
        try {
            return queryRunner.update(connection, sqlProperties.getProperty(ADD_ORDER_ITEM), orderId, orderItem.getProduct().getId(), orderItem.getQuantity());
        } catch (SQLException ex) {
            putErrorMsgToConnection(ex.getMessage(), connection);
        }
        return 0;
    }

    @Override
    public long createNewOrder(long userId) {
        Connection connection = ThreadLocalConnection.getConnection();
        try {
            return queryRunner.insert(connection, sqlProperties.getProperty(CREATE_NEW_ORDER), new ScalarHandler<BigInteger>(), userId, "newly").longValue();
        } catch (SQLException ex) {
            putErrorMsgToConnection(ex.getMessage(), connection);
        }
        return -1;
    }
}