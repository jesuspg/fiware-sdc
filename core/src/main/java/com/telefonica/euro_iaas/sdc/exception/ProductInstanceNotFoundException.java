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

import com.telefonica.euro_iaas.sdc.model.ProductInstance;

/**
 * Exception thrown when trying to delete a ProductRelease that does not exist.
 * 
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class ProductInstanceNotFoundException extends Exception {

    private List<ProductInstance> productInstances;

    public ProductInstanceNotFoundException() {
        super();
    }

    public ProductInstanceNotFoundException(List<ProductInstance> productInstances) {
        this.productInstances = productInstances;
    }

    public ProductInstanceNotFoundException(String msg) {
        super(msg);
    }

    public ProductInstanceNotFoundException(Throwable e) {
        super(e);
    }

    public ProductInstanceNotFoundException(String msg, Throwable e) {
        super(msg, e);
    }

    /**
     * @return the productInstances
     */
    public List<ProductInstance> getProductReleases() {
        return productInstances;
    }

    /**
     * @param productInstances
     *            the productInstances to set
     */
    public void setProductReleases(List<ProductInstance> productInstances) {
        this.productInstances = productInstances;
    }
}
