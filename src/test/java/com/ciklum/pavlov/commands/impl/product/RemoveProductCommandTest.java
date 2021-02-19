package com.ciklum.pavlov.commands.impl.product;

import com.ciklum.pavlov.context.ApplicationContext;
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

import static com.ciklum.pavlov.commands.impl.product.RemoveProductCommand.PRODUCT_REMOVE_FAILED;
import static com.ciklum.pavlov.commands.impl.product.RemoveProductCommand.PRODUCT_REMOVE_SUCCEED;
import static com.ciklum.pavlov.constants.UIKeys.ONE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RemoveProductCommandTest {
    @Mock
    private ProductService productService;
    @Mock
    private ApplicationContext context;
    @Mock
    private RemoveProductCommand removeProductCommand;
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
        removeProductCommand = new RemoveProductCommand(context, productService);
    }

    @Test
    public void should_PrintSuccessNote_When_ProductRemoved() {
        when(customReader.readLine()).thenReturn(ONE);
        when(productService.removeProduct(1)).thenReturn(Boolean.TRUE);
        removeProductCommand.execute();
        verify(resourceBundle).getString(PRODUCT_REMOVE_SUCCEED);
    }

    @Test
    public void should_PrintFailNote_When_ProductNotRemoved() {
        when(customReader.readLine()).thenReturn(ONE);
        when(productService.removeProduct(1)).thenReturn(false);
        removeProductCommand.execute();
        verify(resourceBundle).getString(PRODUCT_REMOVE_FAILED);
    }
}