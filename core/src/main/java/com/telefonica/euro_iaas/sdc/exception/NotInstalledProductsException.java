package com.telefonica.euro_iaas.sdc.exception;

import java.util.List;

import com.telefonica.euro_iaas.sdc.model.ProductRelease;

/**
 * Exception thrown when try to install an application and the needed products
 * are not installed.
 *
 * @author Sergio Arroyo
 */
@SuppressWarnings("serial")
public class NotInstalledProductsException extends Exception {

    private List<ProductRelease> products;

    /**
     * @param applications
     * @param product
     */
    public NotInstalledProductsException(List<ProductRelease> products) {
        this.products = products;

    }

    private String listProducts(List<ProductRelease> products) {
        String productList = "";
        for (ProductRelease product : products) {
            productList = productList.concat(", "
                    + product.getProduct().getName()
                    + "-" + product.getVersion());
        }
        return productList;
    }

    @Override
    public String getMessage() {
        return "These products are not installed "
            + listProducts(products);
    }

}
