package com.ciklum.pavlov.services.impl;

import com.ciklum.pavlov.dao.OrderDao;
import com.ciklum.pavlov.jdbc.TransactionManager;
import com.ciklum.pavlov.models.Order;
import com.ciklum.pavlov.models.Product;
import com.ciklum.pavlov.models.User;
import com.ciklum.pavlov.services.OrderService;
import com.ciklum.pavlov.services.ProductService;

import java.util.Optional;

import static com.ciklum.pavlov.models.Order.OrderItem;

public class OrderServiceImpl implements OrderService {
    private final ProductService productService;
    private final OrderDao orderDao;
    private final TransactionManager transactionManager;

    public OrderServiceImpl(OrderDao orderDao, ProductService productService, TransactionManager transactionManager) {
        this.orderDao = orderDao;
        this.transactionManager = transactionManager;
        this.productService = productService;
    }

    @Override
    public Optional<Order> getCurrentOrder(long userId) {
        return transactionManager.executeInTransaction(() -> orderDao.getOrder(userId));
    }

    @Override
    public boolean updateOrderItemCount(long productId, int quantity, User user) {
        Optional<Order> order = getCurrentOrder(user.getId());
        return order.filter(value -> transactionManager.executeInTransaction(() -> orderDao.updateOrderItemQuantity(productId, value.getId(), quantity)) > 0).isPresent();
    }

    @Override
    public boolean addProductToOrder(long productIdentifier, int quantity, long userId) {
        Optional<Order> userOrder = getCurrentOrder(userId);
        Optional<Product> product = productService.getProduct(productIdentifier);
        if (!product.isPresent()) {
            return false;
        }
        int inserted;
        if (!userOrder.isPresent()) {
            inserted = transactionManager.executeInTransaction(() -> {
                long newOrderId = orderDao.createNewOrder(userId);
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product.get());
                orderItem.setQuantity(quantity);
                return orderDao.addOrderItem(orderItem, newOrderId);
            });
        } else {
            inserted = transactionManager.executeInTransaction(() -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product.get());
                orderItem.setQuantity(quantity);
                return orderDao.addOrderItem(orderItem, userOrder.get().getId());
            });
        }
        return inserted > 0;
    }
}
