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
 * Exception thrown when trying to delete a ProductRelease that does not exist.
 * 
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class ProductReleaseNotFoundException extends Exception {

    private List<ProductRelease> productReleases;

    public ProductReleaseNotFoundException() {
        super();
    }

    public ProductReleaseNotFoundException(List<ProductRelease> productReleases) {
        this.productReleases = productReleases;
    }

    public ProductReleaseNotFoundException(String msg) {
        super(msg);
    }

    public ProductReleaseNotFoundException(Throwable e) {
        super(e);
    }

    public ProductReleaseNotFoundException(String msg, Throwable e) {
        super(msg, e);
    }

    /**
     * @return the productRelease
     */
    public List<ProductRelease> getProductReleases() {
        return productReleases;
    }

    /**
     * @param productRelease
     *            the productRelease to set
     */
    public void setProductReleases(List<ProductRelease> productReleases) {
        this.productReleases = productReleases;
    }
}
