package com.ciklum.pavlov.commands.impl.order;

import com.ciklum.pavlov.context.ApplicationContext;
import com.ciklum.pavlov.models.Order;
import com.ciklum.pavlov.models.Product;
import com.ciklum.pavlov.models.User;
import com.ciklum.pavlov.services.OrderService;
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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ciklum.pavlov.commands.impl.order.DisplayOrderCommand.EMPTY_ORDER_NOTE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DisplayOrderCommandTest {
    @Mock
    private OrderService orderService;
    @Mock
    private ApplicationContext context;
    @Mock
    private DisplayOrderCommand displayOrderCommand;
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
        displayOrderCommand = new DisplayOrderCommand(context, orderService);
    }

    @Test
    public void should_PrintEmptyOrderNote_When_OrderIsNotExists() {
        User currentUser = new User(1);
        when(context.getCurrentUser()).thenReturn(currentUser);
        when(orderService.getCurrentOrder(1)).thenReturn(Optional.empty());
        displayOrderCommand.execute();
        verify(customWriter).println(resourceBundle.getString(EMPTY_ORDER_NOTE));
    }

    @Test
    public void should_PrintOrderIntoOutputStream_When_OrderExists() {
        User currentUser = new User(1);
        Order.OrderItem orderItem = new Order.OrderItem();
        orderItem.setProduct(getValidProduct());
        orderItem.setQuantity(1);
        Order order = new Order();
        order.setId(1);
        order.setOrderItems(Stream.of(orderItem).collect(Collectors.toSet()));
        when(context.getCurrentUser()).thenReturn(currentUser);
        when(orderService.getCurrentOrder(1)).thenReturn(Optional.of(order));
        when(customWriter.getInnerOutputStream()).thenReturn(Mockito.mock(OutputStream.class));
        when(customWriter.getCharset()).thenReturn(StandardCharsets.UTF_8);
        displayOrderCommand.execute();
        verify(resourceBundle).getString(Order.ORDER_ID);
    }

    private Product getValidProduct() {
        Product product = new Product();
        product.setName("product");
        return product;
    }
}