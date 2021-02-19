package com.ciklum.pavlov.models;

import com.ciklum.pavlov.util.io.CustomWriter;
import dnl.utils.text.table.TextTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.Set;
@Slf4j
@Data
@EqualsAndHashCode
public class Order {
    public static final String ORDER_ID = "main.order.id";
    public static final String PRODUCT_TOTAL_PRICE = "main.order.product.total";
    public static final String PRODUCT_NAME = "main.order.product.name";
    public static final String PRODUCT_QUANTITY = "main.order.product.count";
    public static final String CREATED_DATE = "main.order.createAt";
    private long id;
    private Set<OrderItem> orderItems;
    private LocalDateTime createdAt;

    @Slf4j
    @Data
    @EqualsAndHashCode
    public static class OrderItem {
        private Product product;
        private int quantity;
    }

    public static void printOrder(Order order, CustomWriter writer, ResourceBundle resourceBundle) {
        Object[][] data = order.orderItems.stream().map((el) -> new Object[]{order.getId(),
                calculateTotal(el),
                el.getProduct().getName(),
                el.getQuantity(),
                order.getCreatedAt()}).toArray(Object[][]::new);
        String[] header = new String[]{
                resourceBundle.getString(ORDER_ID),
                resourceBundle.getString(PRODUCT_TOTAL_PRICE),
                resourceBundle.getString(PRODUCT_NAME),
                resourceBundle.getString(PRODUCT_QUANTITY),
                resourceBundle.getString(CREATED_DATE)};
        try {
            new TextTable(header, data).printTable(new PrintStream(writer.getInnerOutputStream(), true, writer.getCharset().name()), 0);
        } catch (UnsupportedEncodingException e) {
            log.error("Illegal charset value has been passed!");
            throw new IllegalStateException();
        }
    }

    private static int calculateTotal(Order.OrderItem orderItem) {
        return orderItem.getProduct().getPrice() * orderItem.getQuantity();
    }

}
