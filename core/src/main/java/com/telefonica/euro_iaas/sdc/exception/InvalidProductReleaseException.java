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

import com.telefonica.euro_iaas.sdc.model.ProductRelease;

/**
 * Exception thrown when trying to insert a ProductRelease that does not have the right information
 * 
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class InvalidProductReleaseException extends Exception {

    private ProductRelease productRelease;

    public InvalidProductReleaseException() {
        super();
    }

    public InvalidProductReleaseException(ProductRelease productRelease) {
        this.productRelease = productRelease;
    }

    public InvalidProductReleaseException(String msg) {
        super(msg);
    }

    public InvalidProductReleaseException(Throwable e) {
        super(e);
    }

    public InvalidProductReleaseException(String msg, Throwable e) {
        super(msg, e);
    }

    /**
     * @return the productRelease
     */
    public ProductRelease getProductRelease() {
        return productRelease;
    }

    /**
     * @param productRelease
     *            the productRelease to set
     */
    public void setProductRelease(ProductRelease productRelease) {
        this.productRelease = productRelease;
    }
}
