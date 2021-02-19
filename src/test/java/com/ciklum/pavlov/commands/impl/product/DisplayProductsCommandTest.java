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
import org.mockito.Mockito;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DisplayProductsCommandTest {
    @Mock
    private ProductService productService;
    @Mock
    private ApplicationContext context;
    @Mock
    private DisplayProductsCommand displayProductsCommand;
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
        displayProductsCommand = new DisplayProductsCommand(context, productService);
    }

    @Test
    public void Should_PrintEmptyProductListNote_When_NoProductsExist() {
        when(productService.getAllProducts()).thenReturn(Collections.emptyList());
        displayProductsCommand.execute();
        verify(customWriter).println(resourceBundle.getString(EMPTY_PRODUCT_LIST));
    }

    @Test
    public void Should_PrintProductList_When_ProductsExist() {
        Product product = new Product();
        product.setProductStatus(Product.ProductStatus.in_stock);
        product.setName("product");
        product.setId(1);
        product.setPrice(1);
        product.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        when(productService.getAllProducts()).thenReturn(Stream.of(product).collect(Collectors.toList()));
        when(customWriter.getInnerOutputStream()).thenReturn(Mockito.mock(OutputStream.class));
        when(customWriter.getCharset()).thenReturn(StandardCharsets.UTF_8);
        displayProductsCommand.execute();
        verify(resourceBundle).getString(PRODUCT_NAME);
        verify(resourceBundle).getString(PRODUCT_PRICE);
        verify(resourceBundle).getString(PRODUCT_STATUS);
    }
}