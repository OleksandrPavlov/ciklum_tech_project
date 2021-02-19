package com.ciklum.pavlov.commands.impl.product;

import com.ciklum.pavlov.commands.AbstractCommand;
import com.ciklum.pavlov.context.ApplicationContext;
import com.ciklum.pavlov.models.Product;
import com.ciklum.pavlov.services.ProductService;

import java.util.List;
import java.util.ResourceBundle;

public class DisplayOrderedProductsCommand extends AbstractCommand {
    public static final String EMPTY_PRODUCT_LIST = "commands.product.showAll.empty";
    protected final ProductService productService;

    public DisplayOrderedProductsCommand(ApplicationContext context, ProductService productService) {
        super(context);
        this.productService = productService;
    }

    @Override
    public boolean execute() {
        ResourceBundle resourceBundle = getResourceBundle();
        List<Product> products = productService.getAllOrderedProducts();
        if (products.isEmpty()) {
            writer.println(resourceBundle.getString(EMPTY_PRODUCT_LIST));
            return Boolean.TRUE;
        }
        Product.printProductList(products, writer, resourceBundle);
        return Boolean.TRUE;
    }

}
