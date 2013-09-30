/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */

package com.telefonica.euro_iaas.sdc.exception;

import java.util.List;

import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;

/**
 * Exception thrown when try to uninstall a product that has any application running on it.
 * 
 * @author Sergio Arroyo
 */
@SuppressWarnings("serial")
public class IncompatibleProductsException extends Exception {

    private ApplicationInstance application;
    private List<ProductInstance> products;

    /**
     * @param applications
     * @param product
     */
    public IncompatibleProductsException(ApplicationInstance application, List<ProductInstance> products) {
        this.application = application;
        this.products = products;

    }

    private String listProducts(List<ProductInstance> products) {
        String productList = "";
        for (ProductInstance product : products) {
            productList = productList.concat(", " + product.getProductRelease().getProduct().getName() + "-"
                    + product.getProductRelease().getVersion());
        }
        return productList;
    }

    @Override
    public String getMessage() {
        return "The application " + application.getApplication().getApplication().getName() + "-"
                + application.getApplication().getVersion() + "can not be installed in these products: "
                + listProducts(products);
    }

    /**
     * @return the application
     */
    public ApplicationInstance getApplication() {
        return application;
    }

    /**
     * @param application
     *            the application to set
     */
    public void setApplication(ApplicationInstance application) {
        this.application = application;
    }

    /**
     * @return the products
     */
    public List<ProductInstance> getProducts() {
        return products;
    }

    /**
     * @param products
     *            the products to set
     */
    public void setProducts(List<ProductInstance> products) {
        this.products = products;
    }

}
