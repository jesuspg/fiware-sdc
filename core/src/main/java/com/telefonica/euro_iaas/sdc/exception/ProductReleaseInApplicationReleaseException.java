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

import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;

/**
 * Exception thrown when try to delete a ProductRelease which is included in any ApplicationRelease object
 * 
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class ProductReleaseInApplicationReleaseException extends Exception {

    private List<ApplicationRelease> applicationReleases;
    private ProductRelease productRelease;

    public ProductReleaseInApplicationReleaseException() {
        super();
    }

    public ProductReleaseInApplicationReleaseException(ProductRelease productRelease,
            List<ApplicationRelease> applicationReleases) {
        this.applicationReleases = applicationReleases;
        this.productRelease = productRelease;
    }

    public ProductReleaseInApplicationReleaseException(List<ApplicationRelease> applicationReleases) {
        this.applicationReleases = applicationReleases;
    }

    public ProductReleaseInApplicationReleaseException(String msg) {
        super(msg);
    }

    public ProductReleaseInApplicationReleaseException(Throwable e) {
        super(e);
    }

    public ProductReleaseInApplicationReleaseException(String msg, Throwable e) {
        super(msg, e);
    }

    /**
     * @return the productInstances
     */
    public List<ApplicationRelease> getProductInstances() {
        return applicationReleases;
    }

    /**
     * @param applicationReleases
     *            the applicationReleases to set
     */
    public void setProductInstances(List<ApplicationRelease> applicationReleases) {
        this.applicationReleases = applicationReleases;
    }
}
