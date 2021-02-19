package com.ciklum.pavlov.commands.impl.product;

import com.ciklum.pavlov.commands.AbstractCommand;
import com.ciklum.pavlov.context.ApplicationContext;
import com.ciklum.pavlov.models.Product;
import com.ciklum.pavlov.services.ProductService;

import java.util.Optional;
import java.util.ResourceBundle;

import static com.ciklum.pavlov.constants.UIKeys.*;
import static com.ciklum.pavlov.util.PollUtil.*;

public class CreateProductCommand extends AbstractCommand {
    public static final String ENTER_PRODUCT_NAME = "main.product.create.name";
    public static final String ENTER_PRODUCT_PRICE = "main.product.create.price";
    public static final String PRODUCT_CREATING_ERROR = "main.note.error.productCreation";
    public static final String PRODUCT_CREATING_SUCCESS = "main.product.create.success";
    private final ProductService productService;

    public CreateProductCommand(ApplicationContext context, ProductService productService) {
        super(context);
        this.productService = productService;
    }

    @Override
    public boolean execute() {
        ResourceBundle resourceBundle = getResourceBundle();
        writer.println(resourceBundle.getString(ENTER_PRODUCT_NAME));
        String productName = pollString(writer, reader, ANY_WORD, Optional.empty(), Optional.of(resourceBundle));
        writer.println(resourceBundle.getString(ENTER_PRODUCT_PRICE));
        int price = pollPositiveInteger(writer, reader, Optional.of(resourceBundle));
        writer.println(resourceBundle.getString(ENTER_PRODUCT_STATUS));
        Product.ProductStatus productStatus = pollProductStatus(writer, reader, Optional.of(resourceBundle));
        Product product = new Product();
        product.setName(productName);
        product.setPrice(price);
        product.setProductStatus(productStatus);
        int productIdentifier = productService.addProduct(product);
        String notificationMsg = productIdentifier == 0 ? resourceBundle.getString(PRODUCT_CREATING_ERROR) : resourceBundle.getString(PRODUCT_CREATING_SUCCESS);
        writer.println(notificationMsg);
        return Boolean.TRUE;
    }
}
