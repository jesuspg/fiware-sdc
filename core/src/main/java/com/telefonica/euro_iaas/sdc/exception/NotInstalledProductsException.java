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

import com.telefonica.euro_iaas.sdc.model.ProductRelease;

/**
 * Exception thrown when try to install an application and the needed products are not installed.
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
            productList = productList.concat(", " + product.getProduct().getName() + "-" + product.getVersion());
        }
        return productList;
    }

    @Override
    public String getMessage() {
        return "These products are not installed " + listProducts(products);
    }

    /**
     * @return the products
     */
    public List<ProductRelease> getProducts() {
        return products;
    }

}
