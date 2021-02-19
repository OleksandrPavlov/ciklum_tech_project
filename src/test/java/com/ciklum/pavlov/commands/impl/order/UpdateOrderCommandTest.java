package com.ciklum.pavlov.commands.impl.order;

import com.ciklum.pavlov.context.ApplicationContext;
import com.ciklum.pavlov.models.User;
import com.ciklum.pavlov.services.OrderService;
import com.ciklum.pavlov.util.io.CustomReader;
import com.ciklum.pavlov.util.io.CustomWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ResourceBundle;

import static com.ciklum.pavlov.commands.impl.order.UpdateOrderCommand.ORDER_NOT_UPDATED;
import static com.ciklum.pavlov.commands.impl.order.UpdateOrderCommand.ORDER_UPDATED;
import static com.ciklum.pavlov.constants.UIKeys.ONE;
import static com.ciklum.pavlov.constants.UIKeys.TWO;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateOrderCommandTest {

    @Mock
    private OrderService orderService;
    @Mock
    private ApplicationContext context;
    @Mock
    private UpdateOrderCommand updateOrderCommand;
    @Mock
    private CustomWriter writer;
    @Mock
    private CustomReader reader;
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("application");

    @Before
    public void setUp() {
        when(context.getWriter()).thenReturn(writer);
        when(context.getReader()).thenReturn(reader);
        when(context.getResourceBundle()).thenReturn(resourceBundle);
        updateOrderCommand = new UpdateOrderCommand(context, orderService);
    }

    @Test
    public void Should_PrintOrderUpdateSuccessNote_When_OrderUpdated() {
        when(reader.readLine()).thenReturn(ONE).thenReturn(TWO);
        User user = new User(1);
        when(context.getCurrentUser()).thenReturn(user);
        when(orderService.updateOrderItemCount(1, 2, user)).thenReturn(Boolean.TRUE);
        updateOrderCommand.execute();
        verify(writer).println(resourceBundle.getString(ORDER_UPDATED));
    }

    @Test
    public void Should_PrintOrderUpdateFailedNote_When_OrderNotUpdated() {
        when(reader.readLine()).thenReturn(ONE).thenReturn(TWO);
        User user = new User(1);
        when(context.getCurrentUser()).thenReturn(user);
        when(orderService.updateOrderItemCount(1, 2, user)).thenReturn(false);
        updateOrderCommand.execute();
        verify(writer).println(resourceBundle.getString(ORDER_NOT_UPDATED));
    }


}