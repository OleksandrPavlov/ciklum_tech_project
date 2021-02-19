package com.ciklum.pavlov.commands.impl.product;

import com.ciklum.pavlov.context.ApplicationContext;
import com.ciklum.pavlov.models.User;
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

import static com.ciklum.pavlov.commands.impl.product.RemoveAllProductsCommand.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RemoveAllProductsCommandTest {
    @Mock
    private ProductService productService;
    @Mock
    private ApplicationContext context;
    @Mock
    private RemoveAllProductsCommand removeAllProductsCommand;
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
        removeAllProductsCommand = new RemoveAllProductsCommand(context, productService);
    }

    @Test
    public void Should_PrintInvalidPasswordMsg_WhenWrongPasswordEntered() {
        String inputPassword = "Invalid1@@";
        String actualPassword = "Actual1@@";
        User currentUser = new User();
        currentUser.setPassword(actualPassword);
        when(customReader.readLine()).thenReturn(inputPassword);
        when(context.getCurrentUser()).thenReturn(currentUser);
        removeAllProductsCommand.execute();
        verify(customWriter).println(resourceBundle.getString(ALL_PRODUCT_REMOVE_FAILED_INVALID_PASSWORD));
    }

    @Test
    public void Should_PrintFailedMsg_When_ProductListAlreadyEmpty() {
        String inputPassword = "Actual1@@";
        String actualPassword = "Actual1@@";
        User currentUser = new User();
        currentUser.setPassword(actualPassword);
        when(customReader.readLine()).thenReturn(inputPassword);
        when(context.getCurrentUser()).thenReturn(currentUser);
        when(productService.removeAllProducts()).thenReturn(0);
        removeAllProductsCommand.execute();
        verify(customWriter).println(resourceBundle.getString(ALL_PRODUCT_REMOVE_FAILED));
    }

    @Test
    public void Should_PrintSuccessMsg_When_ProductListRemoved() {
        String inputPassword = "Actual1@@";
        String actualPassword = "Actual1@@";
        User currentUser = new User();
        currentUser.setPassword(actualPassword);
        when(customReader.readLine()).thenReturn(inputPassword);
        when(context.getCurrentUser()).thenReturn(currentUser);
        when(productService.removeAllProducts()).thenReturn(1);
        removeAllProductsCommand.execute();
        verify(customWriter).println(resourceBundle.getString(ALL_PRODUCTS_REMOVED));
    }
}