package com.ciklum.pavlov.services.impl;

import com.ciklum.pavlov.dao.OrderDao;
import com.ciklum.pavlov.jdbc.TransactionManager;
import com.ciklum.pavlov.models.Order;
import com.ciklum.pavlov.models.Product;
import com.ciklum.pavlov.models.User;
import com.ciklum.pavlov.services.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {
    @Mock
    TransactionManager transactionManager;
    @Mock
    ProductService productService;
    @Mock
    OrderDao orderDao;
    @InjectMocks
    OrderServiceImpl orderService;
    @Captor
    ArgumentCaptor<Supplier> orderCaptor;


    @Test
    public void Should_ReturnNotEmptyOrder_When_GetCurrentOrderMethodCalled() {
        Order order = new Order();
        when(transactionManager.executeInTransaction(any(Supplier.class))).thenReturn(Optional.of(order));
        Optional<Order> optionalOrder = orderService.getCurrentOrder(1);
        verify(transactionManager).executeInTransaction(orderCaptor.capture());
        orderCaptor.getValue().get();
        verify(orderDao).getOrder(1);
        assertTrue(optionalOrder.isPresent());
    }

    @Test
    public void Should_ReturnTrue_When_PromptedOrderExist() {
        Order order = new Order();
        User user = new User();
        when(transactionManager.executeInTransaction(any(Supplier.class)))
                .thenReturn(Optional.of(order))
                .thenReturn(1);
        boolean executionResult = orderService.updateOrderItemCount(1, 1, user);
        assertTrue(executionResult);
    }

    @Test
    public void Should_ReturnFalse_When_PromptedOrderNotExist() {
        Order order = new Order();
        User user = new User();
        when(transactionManager.executeInTransaction(any(Supplier.class)))
                .thenReturn(Optional.of(order))
                .thenReturn(-1);
        boolean executionResult = orderService.updateOrderItemCount(1, 1, user);
        assertFalse(executionResult);
    }

    @Test
    public void Should_ReturnFalse_When_PointedProductNotExist() {
        when(transactionManager.executeInTransaction(any(Supplier.class))).thenReturn(Optional.empty());
        boolean executeStatus = orderService.addProductToOrder(1, 1, 1);
        assertFalse(executeStatus);
    }

    @Test
    public void Should_CreateAndAddNewOrder_When_NoOrderAppliedToUser() {
        Product product = new Product();
        when(transactionManager.executeInTransaction(any(Supplier.class)))
                .thenReturn(Optional.empty())
                .thenReturn(1);
        when(productService.getProduct(1))
                .thenReturn(Optional.of(product));
        when(orderDao.createNewOrder(1)).thenReturn(1l);
        boolean orderAppended = orderService.addProductToOrder(1, 1, 1l);
        verify(transactionManager, Mockito.times(2)).executeInTransaction(orderCaptor.capture());
        List<Supplier> supplierList = orderCaptor.getAllValues();
        supplierList.get(1).get();
        verify(orderDao).createNewOrder(1);
        verify(orderDao).addOrderItem(any(Order.OrderItem.class), any(Long.class));
        assertTrue(orderAppended);
    }

    @Test
    public void Should_OnlyAddOrderItem_When_OrderExists() {
        Product product = new Product();
        when(transactionManager.executeInTransaction(any(Supplier.class)))
                .thenReturn(Optional.of(new Order()))
                .thenReturn(1);
        when(productService.getProduct(1))
                .thenReturn(Optional.of(product));
        boolean orderAppended = orderService.addProductToOrder(1, 1, 1l);
        verify(transactionManager, Mockito.times(2)).executeInTransaction(orderCaptor.capture());
        List<Supplier> supplierList = orderCaptor.getAllValues();
        supplierList.get(1).get();
        verify(orderDao).addOrderItem(any(Order.OrderItem.class), any(Long.class));
        assertTrue(orderAppended);
    }

}