package com.ciklum.pavlov.commands.impl.product;

import com.ciklum.pavlov.commands.AbstractCommand;
import com.ciklum.pavlov.context.ApplicationContext;
import com.ciklum.pavlov.services.ProductService;
import com.ciklum.pavlov.util.PollUtil;

import java.util.Optional;
import java.util.ResourceBundle;

import static com.ciklum.pavlov.constants.UIKeys.PASSWORD_POLL;

public class RemoveAllProductsCommand extends AbstractCommand {
    public static final String REMOVE_ALL_PRODUCT_WARNING = "main.product.removeAll.warning";
    public static final String ALL_PRODUCTS_REMOVED = "main.product.removeAll.succeed";
    public static final String ALL_PRODUCT_REMOVE_FAILED = "main.product.removeAll.failed";
    public static final String ALL_PRODUCT_REMOVE_FAILED_INVALID_PASSWORD = "main.product.removeAll.failed.password";
    private final ProductService productService;

    public RemoveAllProductsCommand(ApplicationContext context, ProductService productService) {
        super(context);
        this.productService = productService;
    }

    @Override
    public boolean execute() {
        ResourceBundle resourceBundle = getResourceBundle();
        writer.println(resourceBundle.getString(REMOVE_ALL_PRODUCT_WARNING));
        writer.println(resourceBundle.getString(PASSWORD_POLL));
        String password = PollUtil.pollString(writer, reader, PollUtil.PASSWORD, Optional.empty(), Optional.of(resourceBundle));
        String notificationMsg = resourceBundle.getString(ALL_PRODUCTS_REMOVED);
        notificationMsg = password.equals(context.getCurrentUser().getPassword()) ?
                productService.removeAllProducts() > 0 ? notificationMsg : resourceBundle.getString(ALL_PRODUCT_REMOVE_FAILED) :
                resourceBundle.getString(ALL_PRODUCT_REMOVE_FAILED_INVALID_PASSWORD);
        writer.println(notificationMsg);
        return Boolean.TRUE;
    }
}
