package com.ciklum.pavlov.commands.impl.order;

import com.ciklum.pavlov.commands.AbstractCommand;
import com.ciklum.pavlov.context.ApplicationContext;
import com.ciklum.pavlov.models.Order;
import com.ciklum.pavlov.services.OrderService;

import java.util.Optional;
import java.util.ResourceBundle;

public class DisplayOrderCommand extends AbstractCommand {
    public static final String SHOW_ORDER_HEADER = "main.order.header";
    public static final String EMPTY_ORDER_NOTE = "main.order.empty";

    private final OrderService orderService;

    public DisplayOrderCommand(ApplicationContext context, OrderService orderService) {
        super(context);
        this.orderService = orderService;
    }

    @Override
    public boolean execute() {
        ResourceBundle resourceBundle = getResourceBundle();
        writer.println(resourceBundle.getString(SHOW_ORDER_HEADER));
        Optional<Order> order = orderService.getCurrentOrder(context.getCurrentUser().getId());
        if (!order.isPresent()) {
            writer.println(resourceBundle.getString(EMPTY_ORDER_NOTE));
        } else {
            Order.printOrder(order.get(), writer, resourceBundle);
        }
        return Boolean.TRUE;
    }
}
