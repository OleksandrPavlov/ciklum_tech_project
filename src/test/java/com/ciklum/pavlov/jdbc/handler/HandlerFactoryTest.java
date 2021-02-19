package com.ciklum.pavlov.jdbc.handler;

import com.ciklum.pavlov.constants.DBConstants;
import com.ciklum.pavlov.models.Order;
import com.ciklum.pavlov.models.Product;
import com.ciklum.pavlov.models.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ciklum.pavlov.constants.DBConstants.*;
import static com.ciklum.pavlov.jdbc.handler.HandlerFactory.*;
import static java.lang.Boolean.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HandlerFactoryTest {
    private static final String CREATED_AT = "1999-02-19 12:23:23";
    private static final String PRODUCT_NAME_ONE = "product";
    private static final Product.ProductStatus PRODUCT_STATUS_IN_STOCK = Product.ProductStatus.in_stock;
    private ResultSet resultSet;
    private static List<Product> productList;

    @BeforeClass
    public static void init() {
        productList = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            Product product = new Product();
            product.setId(i);
            product.setName(PRODUCT_NAME_ONE + i);
            product.setPrice(i);
            product.setProductStatus(PRODUCT_STATUS_IN_STOCK);
            product.setCreatedAt(LocalDateTime.from(DateTimeFormatter.ofPattern(CUSTOM_TIME_PATTERN).parse(CREATED_AT)));
            productList.add(product);
        }

    }

    @Before
    public void beforeTest() {
        resultSet = Mockito.mock(ResultSet.class);
    }

    @Test
    public void Should_GenerateUserFromResultSet_When_handleMethodCalled() throws SQLException {
        String userLogin = "login";
        String userPassword = "password";
        when(resultSet.next()).thenReturn(TRUE);
        when(resultSet.getInt(ID)).thenReturn(1);
        when(resultSet.getString(LOGIN)).thenReturn(userLogin);
        when(resultSet.getString(PASSWORD)).thenReturn(userPassword);
        User expectedUser = new User();
        expectedUser.setId(1);
        expectedUser.setLogin(userLogin);
        expectedUser.setPassword(userPassword);
        Optional<User> actualUser = USER_RESULT_SET_HANDLER.handle(resultSet);
        assertEquals(actualUser.get(), expectedUser);
    }

    @Test
    public void Should_GenerateProductFromResultSet_When_handleMethodCalled() throws SQLException {
        String localDateTime = "1999-02-19 12:23:23";
        when(resultSet.next()).thenReturn(TRUE);
        when(resultSet.getInt(ID)).thenReturn(1);
        when(resultSet.getString(NAME)).thenReturn(PRODUCT_NAME_ONE + 1);
        when(resultSet.getInt(PRICE)).thenReturn(1);
        when(resultSet.getString(PRODUCT_STATUS)).thenReturn(PRODUCT_STATUS_IN_STOCK.name());
        when(resultSet.getString(DBConstants.CREATED_AT)).thenReturn(localDateTime);
        Product expectedProduct = productList.get(0);
        Optional<Product> actualProduct = PRODUCT_RESULT_SET_HANDLER.handle(resultSet);
        assertEquals(actualProduct.get(), expectedProduct);
    }

    @Test
    public void Should_GenerateProductList_When_HandleMethodCalled() throws SQLException {
        String localDateTimeOne = "1999-02-19 12:23:23";
        when(resultSet.next()).thenReturn(TRUE)
                .thenReturn(TRUE)
                .thenReturn(FALSE);
        when(resultSet.getInt(ID)).thenReturn(1).thenReturn(2);
        when(resultSet.getString(NAME)).thenReturn(PRODUCT_NAME_ONE + 1).thenReturn(PRODUCT_NAME_ONE + 2);
        when(resultSet.getInt(PRICE)).thenReturn(1).thenReturn(2);
        when(resultSet.getString(PRODUCT_STATUS)).thenReturn(PRODUCT_STATUS_IN_STOCK.name());
        when(resultSet.getString(DBConstants.CREATED_AT)).thenReturn(localDateTimeOne).thenReturn(CREATED_AT);
        List<Product> actualProductList = PRODUCT_LIST_HANDLER.handle(resultSet);
        assertThat(actualProductList).hasSameElementsAs(productList);
    }

    @Test
    public void Should_GenerateOrder_When_HandleMethodCalled() throws SQLException {
        Order expectedOrder = new Order();
        expectedOrder.setId(1);
        expectedOrder.setCreatedAt(LocalDateTime.from(DateTimeFormatter.ofPattern(CUSTOM_TIME_PATTERN).parse(CREATED_AT)));
        Set<Order.OrderItem> orderItems = productList.stream().map(product -> {
            Order.OrderItem orderItem = new Order.OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(product.getPrice());
            return orderItem;
        }).collect(Collectors.toSet());
        expectedOrder.setOrderItems(orderItems);

        when(resultSet.next()).thenReturn(TRUE)
                .thenReturn(TRUE)
                .thenReturn(FALSE);
        when(resultSet.getLong(ORDER_ID)).thenReturn(1l);
        when(resultSet.getString(ORDER_CREATED)).thenReturn(CREATED_AT);
        when(resultSet.getLong(PRODUCT_ID)).thenReturn(1l).thenReturn(2l);
        when(resultSet.getString(PRODUCT_NAME)).thenReturn(PRODUCT_NAME_ONE + 1).thenReturn(PRODUCT_NAME_ONE + 2);
        when(resultSet.getString(PRODUCT_STATUS)).thenReturn(PRODUCT_STATUS_IN_STOCK.name());
        when(resultSet.getString(PRODUCT_CREATED)).thenReturn(CREATED_AT);
        when(resultSet.getInt(PRODUCT_PRICE)).thenReturn(1).thenReturn(2);
        when(resultSet.getInt(PRODUCT_QUANTITY)).thenReturn(1).thenReturn(2);
        Optional<Order> actualOrder = ORDER_HANDLER.handle(resultSet);
        assertThat(actualOrder.get()).isEqualTo(expectedOrder);
    }
}