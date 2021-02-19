package com.ciklum.pavlov.jdbc.handler;

import com.ciklum.pavlov.models.Order;
import com.ciklum.pavlov.models.Product;
import com.ciklum.pavlov.models.User;
import org.apache.commons.dbutils.ResultSetHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.ciklum.pavlov.constants.DBConstants.*;

public class HandlerFactory {
    private HandlerFactory() {

    }

    public static final String CUSTOM_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final ResultSetHandler<Optional<User>> USER_RESULT_SET_HANDLER = (rs) -> {
        User user = null;
        if (rs.next()) {
            user = new User();
            user.setId(rs.getInt(ID));
            user.setLogin(rs.getString(LOGIN));
            user.setPassword(rs.getString(PASSWORD));
        }
        return Optional.ofNullable(user);
    };

    public static final ResultSetHandler<Optional<Product>> PRODUCT_RESULT_SET_HANDLER = (rs) -> {
        Product product = null;
        if (rs.next()) {
            product = new Product();
            product.setId(rs.getInt(ID));
            product.setName(rs.getString(NAME));
            product.setPrice(rs.getInt(PRICE));
            product.setCreatedAt(LocalDateTime.from(DateTimeFormatter.ofPattern(CUSTOM_TIME_PATTERN).parse(rs.getString(CREATED_AT))));
            product.setProductStatus(Product.ProductStatus.valueOf(rs.getString(PRODUCT_STATUS)));
        }
        return Optional.ofNullable(product);
    };

    public static final ResultSetHandler<List<Product>> PRODUCT_LIST_HANDLER = (rs) -> {
        List<Product> list = new ArrayList<>();
        Optional<Product> product;
        while ((product = PRODUCT_RESULT_SET_HANDLER.handle(rs)).isPresent()) {
            product.ifPresent(list::add);
        }
        return list;
    };

    public static final ResultSetHandler<Optional<Order>> ORDER_HANDLER = (rs) -> {
        Order order = null;
        if (rs.next()) {
            order = new Order();
            order.setId(rs.getLong(ORDER_ID));
            order.setCreatedAt(LocalDateTime.from(DateTimeFormatter.ofPattern(CUSTOM_TIME_PATTERN).parse(rs.getString(ORDER_CREATED))));
            Set<Order.OrderItem> orderItems = new HashSet<>();
            do {
                Order.OrderItem orderItem = new Order.OrderItem();
                Product product = new Product();
                product.setId(rs.getLong(PRODUCT_ID));
                product.setName(rs.getString(PRODUCT_NAME));
                product.setProductStatus(Product.ProductStatus.valueOf(rs.getString(PRODUCT_STATUS)));
                product.setCreatedAt(LocalDateTime.from(DateTimeFormatter.ofPattern(CUSTOM_TIME_PATTERN).parse(rs.getString(PRODUCT_CREATED))));
                product.setPrice(rs.getInt(PRODUCT_PRICE));
                orderItem.setProduct(product);
                orderItem.setQuantity(rs.getInt(PRODUCT_QUANTITY));
                orderItems.add(orderItem);
            } while (rs.next());
            order.setOrderItems(orderItems);
        }
        return Optional.ofNullable(order);
    };
}
