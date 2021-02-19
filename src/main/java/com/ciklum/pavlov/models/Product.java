package com.ciklum.pavlov.models;

import com.ciklum.pavlov.util.io.CustomWriter;
import dnl.utils.text.table.TextTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
@Slf4j
@Data
@EqualsAndHashCode
public class Product {
    public static final String PRODUCT_NAME = "commands.product.name";
    public static final String PRODUCT_PRICE = "commands.product.price";
    public static final String PRODUCT_STATUS = "commands.product.status";
    public static final String PRODUCT_ID = "commands.product.id";
    private long id;
    private String name;
    private int price;
    private ProductStatus productStatus;
    private LocalDateTime createdAt;

    public enum ProductStatus {
        out_of_stock,
        in_stock,
        running_low
    }

    public static void printProductList(List<Product> products, CustomWriter writer, ResourceBundle resourceBundle) {
        Object[][] array = products
                .stream()
                .map(pr -> new Object[]{pr.getId(), pr.getName(), pr.getPrice(), pr.getProductStatus()})
                .toArray(Object[][]::new);
        TextTable tt = new TextTable(new String[]{resourceBundle.getString(PRODUCT_ID),
                resourceBundle.getString(PRODUCT_NAME),
                resourceBundle.getString(PRODUCT_PRICE),
                resourceBundle.getString(PRODUCT_STATUS)},
                array);
        try {
            tt.printTable(new PrintStream(writer.getInnerOutputStream(), true, writer.getCharset().name()), 0);
        } catch (UnsupportedEncodingException e) {
            log.error("Illegal charset value has been passed!");
            throw new IllegalStateException();
        }
    }
}
