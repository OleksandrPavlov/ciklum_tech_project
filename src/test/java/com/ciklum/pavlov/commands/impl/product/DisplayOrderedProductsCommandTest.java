package com.ciklum.pavlov.commands.impl.product;

import com.ciklum.pavlov.context.ApplicationContext;
import com.ciklum.pavlov.models.Product;
import com.ciklum.pavlov.services.ProductService;
import com.ciklum.pavlov.util.io.CustomReader;
import com.ciklum.pavlov.util.io.CustomWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ciklum.pavlov.commands.impl.product.DisplayOrderedProductsCommand.EMPTY_PRODUCT_LIST;
import static com.ciklum.pavlov.models.Product.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DisplayOrderedProductsCommandTest {
    @Mock
    private ProductService productService;
    @Mock
    private ApplicationContext context;
    @Mock
    private DisplayOrderedProductsCommand displayOrderedProductsCommand;
    @Mock
    private CustomWriter customWriter;
    @Mock
    private CustomReader customReader;
    @Spy
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("application");


    @Before
    public void setUp() {
        when(context.getWriter()).thenReturn(customWriter);
        when(context.getReader()).thenReturn(customReader);
        when(context.getResourceBundle()).thenReturn(resourceBundle);
        displayOrderedProductsCommand = new DisplayOrderedProductsCommand(context, productService);
    }

    @Test
    public void Should_PrintEmptyMsg_When_OrderedProductListIsEmpty() {
        when(productService.getAllOrderedProducts()).thenReturn(Collections.emptyList());
        displayOrderedProductsCommand.execute();
        verify(customWriter).println(resourceBundle.getString(EMPTY_PRODUCT_LIST));
    }

    @Test
    public void Should_PrintProducts_When_OrderedProductListExists() {
        Product product = new Product();
        product.setProductStatus(Product.ProductStatus.in_stock);
        product.setName("product");
        product.setId(1);
        product.setPrice(1);
        product.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        when(productService.getAllOrderedProducts()).thenReturn(Stream.of(product).collect(Collectors.toList()));
        when(customWriter.getInnerOutputStream()).thenReturn(mock(OutputStream.class));
        when(customWriter.getCharset()).thenReturn(StandardCharsets.UTF_8);
        displayOrderedProductsCommand.execute();
        verify(resourceBundle).getString(PRODUCT_NAME);
        verify(resourceBundle).getString(PRODUCT_PRICE);
        verify(resourceBundle).getString(PRODUCT_STATUS);
    }
}