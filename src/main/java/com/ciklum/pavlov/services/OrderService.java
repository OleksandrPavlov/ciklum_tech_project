package com.ciklum.pavlov.services;

import com.ciklum.pavlov.models.Order;
import com.ciklum.pavlov.models.User;

import java.util.Optional;

public interface OrderService {
    Optional<Order> getCurrentOrder(long userId);

    boolean updateOrderItemCount(long productId, int quantity, User user);

    boolean addProductToOrder(long productId, int quantity, long userId);
}
