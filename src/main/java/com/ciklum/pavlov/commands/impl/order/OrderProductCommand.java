package com.ciklum.pavlov.commands.impl.order;

import com.ciklum.pavlov.commands.AbstractCommand;
import com.ciklum.pavlov.context.ApplicationContext;
import com.ciklum.pavlov.services.OrderService;

import java.util.Optional;
import java.util.ResourceBundle;

import static com.ciklum.pavlov.constants.NotificationConstants.ENTER_PRODUCT_IDENTIFIER;
import static com.ciklum.pavlov.util.PollUtil.pollPositiveInteger;

public class OrderProductCommand extends AbstractCommand {
    public static final String ENTER_PRODUCT_COUNT = "main.order.note.input.productCount";

    private final OrderService orderService;


    public OrderProductCommand(ApplicationContext context, OrderService orderService) {
        super(context);
        this.orderService = orderService;
    }

    @Override
    public boolean execute() {
        ResourceBundle resourceBundle = getResourceBundle();
        writer.println(resourceBundle.getString(ENTER_PRODUCT_IDENTIFIER));
        int productIdentifier = pollPositiveInteger(writer, reader, Optional.of(resourceBundle));
        writer.println(resourceBundle.getString(ENTER_PRODUCT_COUNT));
        int productQuantity = pollPositiveInteger(writer, reader, Optional.of(resourceBundle));
        new Thread(new OrderItemCreator(productIdentifier, productQuantity)).start();
        return Boolean.TRUE;
    }

    private class OrderItemCreator implements Runnable {
        private final long productIdentifier;
        private final int productQuantity;

        public OrderItemCreator(long productIdentifier, int productQuantity) {
            this.productIdentifier = productIdentifier;
            this.productQuantity = productQuantity;
        }

        @Override
        public void run() {
            orderService.addProductToOrder(productIdentifier, productQuantity, context.getCurrentUser().getId());
        }
    }
}
