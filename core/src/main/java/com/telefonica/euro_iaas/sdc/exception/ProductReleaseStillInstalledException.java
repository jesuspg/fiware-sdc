/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.exception;

import java.util.List;

import com.telefonica.euro_iaas.sdc.model.ProductInstance;

/**
 * Exception thrown when try to delete a ProductRelease which is installed on some VMs
 * 
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class ProductReleaseStillInstalledException extends Exception {

    private List<ProductInstance> productInstances;

    public ProductReleaseStillInstalledException() {
        super();
    }

    public ProductReleaseStillInstalledException(List<ProductInstance> productInstances) {
        this.productInstances = productInstances;
    }

    public ProductReleaseStillInstalledException(String msg) {
        super(msg);
    }

    public ProductReleaseStillInstalledException(Throwable e) {
        super(e);
    }

    public ProductReleaseStillInstalledException(String msg, Throwable e) {
        super(msg, e);
    }

    /**
     * @return the productInstances
     */
    public List<ProductInstance> getProductInstances() {
        return productInstances;
    }

    /**
     * @param productInstances
     *            the productInstances to set
     */
    public void setProductInstances(List<ProductInstance> productInstances) {
        this.productInstances = productInstances;
    }

}
