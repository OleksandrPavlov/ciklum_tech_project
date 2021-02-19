package com.ciklum.pavlov.commands.impl.product;

import com.ciklum.pavlov.context.ApplicationContext;
import com.ciklum.pavlov.models.Product;
import com.ciklum.pavlov.services.ProductService;

import java.util.List;
import java.util.ResourceBundle;

public class DisplayProductsCommand extends DisplayOrderedProductsCommand {
    public DisplayProductsCommand(ApplicationContext context, ProductService productService) {
        super(context, productService);
    }

    @Override
    public boolean execute() {
        ResourceBundle resourceBundle = getResourceBundle();
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            writer.println(resourceBundle.getString(EMPTY_PRODUCT_LIST));
            return Boolean.TRUE;
        }
        Product.printProductList(products, writer, resourceBundle);
        return Boolean.TRUE;
    }
}
