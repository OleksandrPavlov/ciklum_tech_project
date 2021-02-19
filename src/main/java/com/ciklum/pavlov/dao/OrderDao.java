package com.ciklum.pavlov.dao;

import com.ciklum.pavlov.models.Order;

import java.util.Optional;

public interface OrderDao {
    int updateOrderItemQuantity(long productId, long orderId, int quantity);

    Optional<Order> getOrder(long userId);

    int addOrderItem(Order.OrderItem orderItem,long orderId);

    long createNewOrder(long userId);
}
