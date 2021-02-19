package com.ciklum.pavlov.commands.impl.order;

import com.ciklum.pavlov.commands.AbstractCommand;
import com.ciklum.pavlov.context.ApplicationContext;
import com.ciklum.pavlov.services.OrderService;
import com.ciklum.pavlov.util.PollUtil;

import java.util.Optional;
import java.util.ResourceBundle;

import static com.ciklum.pavlov.constants.NotificationConstants.ENTER_ORDER_ITEM_QUANTITY;
import static com.ciklum.pavlov.constants.NotificationConstants.ENTER_PRODUCT_IDENTIFIER;

public class UpdateOrderCommand extends AbstractCommand {
    public static final String UPDATE_ORDER_HEADER = "main.order.update.header";
    public static final String ORDER_UPDATED = "main.order.update.success";
    public static final String ORDER_NOT_UPDATED = "main.order.update.failed";
    private final OrderService orderService;

    public UpdateOrderCommand(ApplicationContext context, OrderService orderService) {
        super(context);
        this.orderService = orderService;
    }

    @Override
    public boolean execute() {
        ResourceBundle resourceBundle = getResourceBundle();
        writer.println(resourceBundle.getString(UPDATE_ORDER_HEADER));
        writer.println(resourceBundle.getString(ENTER_PRODUCT_IDENTIFIER));
        int orderItemIdentifier = PollUtil.pollPositiveInteger(writer, reader, Optional.of(resourceBundle));
        writer.println(resourceBundle.getString(ENTER_ORDER_ITEM_QUANTITY));
        int orderItemQuantity = PollUtil.pollPositiveInteger(writer, reader, Optional.of(resourceBundle));
        String notificationMsg = orderService.updateOrderItemCount(orderItemIdentifier, orderItemQuantity, context.getCurrentUser()) ?
                resourceBundle.getString(ORDER_UPDATED) :
                resourceBundle.getString(ORDER_NOT_UPDATED);
        writer.println(notificationMsg);
        return Boolean.TRUE;
    }
}
