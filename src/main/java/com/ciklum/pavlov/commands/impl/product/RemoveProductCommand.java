package com.ciklum.pavlov.commands.impl.product;

import com.ciklum.pavlov.commands.AbstractCommand;
import com.ciklum.pavlov.constants.NotificationConstants;
import com.ciklum.pavlov.context.ApplicationContext;
import com.ciklum.pavlov.services.ProductService;
import com.ciklum.pavlov.util.PollUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.ResourceBundle;

@Slf4j
public class RemoveProductCommand extends AbstractCommand {
    public static final String PRODUCT_REMOVE_SUCCEED = "main.product.remove.succeed";
    public static final String PRODUCT_REMOVE_FAILED = "main.product.remove.failed";
    private final ProductService productService;

    public RemoveProductCommand(ApplicationContext context, ProductService productService) {
        super(context);
        this.productService = productService;
    }

    @Override
    public boolean execute() {
        ResourceBundle resourceBundle = getResourceBundle();
        writer.println(resourceBundle.getString(NotificationConstants.ENTER_PRODUCT_IDENTIFIER));
        int productIdentifier = PollUtil.pollPositiveInteger(writer, reader, Optional.of(resourceBundle));
        String notificationMsg = productService.removeProduct(productIdentifier)
                ? resourceBundle.getString(PRODUCT_REMOVE_SUCCEED)
                : resourceBundle.getString(PRODUCT_REMOVE_FAILED);
        writer.println(notificationMsg);
        return Boolean.TRUE;
    }
}
