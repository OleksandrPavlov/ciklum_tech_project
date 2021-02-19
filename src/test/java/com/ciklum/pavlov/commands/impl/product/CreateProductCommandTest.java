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

import java.util.ResourceBundle;

import static com.ciklum.pavlov.commands.impl.product.CreateProductCommand.PRODUCT_CREATING_ERROR;
import static com.ciklum.pavlov.commands.impl.product.CreateProductCommand.PRODUCT_CREATING_SUCCESS;
import static com.ciklum.pavlov.constants.UIKeys.ONE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateProductCommandTest {

    @Mock
    private ProductService productService;
    @Mock
    private ApplicationContext context;
    @Mock
    private CreateProductCommand createProductCommand;
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
        createProductCommand = new CreateProductCommand(context, productService);
    }

    @Test
    public void Should_PrintSuccessMsg_When_ProductAppendedToOrder() {
        String validProductName = "product";
        when(customReader.readLine()).thenReturn(validProductName)
                .thenReturn(ONE)
                .thenReturn(Product.ProductStatus.in_stock.name());
        when(productService.addProduct(any(Product.class))).thenReturn(1);
        createProductCommand.execute();
        verify(customWriter).println(resourceBundle.getString(PRODUCT_CREATING_SUCCESS));
    }

    @Test
    public void Should_PrintFailMsg_When_ProductAppendedToOrder() {
        String validProductName = "product";
        when(customReader.readLine()).thenReturn(validProductName)
                .thenReturn(ONE)
                .thenReturn(Product.ProductStatus.in_stock.name());
        when(productService.addProduct(any(Product.class))).thenReturn(0);
        createProductCommand.execute();
        verify(customWriter).println(resourceBundle.getString(PRODUCT_CREATING_ERROR));
    }
}