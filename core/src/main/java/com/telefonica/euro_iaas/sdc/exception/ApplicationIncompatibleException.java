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
import com.telefonica.euro_iaas.sdc.model.ProductRelease;

/**
 * Exception thrown when try to uninstall a product that has any application running on it.
 * 
 * @author Sergio Arroyo
 */
@SuppressWarnings("serial")
public class ApplicationIncompatibleException extends Exception {

    private List<ApplicationInstance> applications;
    private ProductRelease product;

    public ApplicationIncompatibleException() {
        super();
    }

    /**
     * @param applications
     * @param product
     */
    public ApplicationIncompatibleException(List<ApplicationInstance> applications, ProductRelease product) {
        super("The product " + product.getProduct().getName() + "-" + product.getVersion() + "has "
                + applications.size() + " applications installed on it.");
        this.applications = applications;
        this.product = product;
    }

    public ApplicationIncompatibleException(String msg) {
        super(msg);
    }

    public ApplicationIncompatibleException(Throwable e) {
        super(e);
    }

    public ApplicationIncompatibleException(String msg, Throwable e) {
        super(msg, e);
    }

    /**
     * @return the applications
     */
    public List<ApplicationInstance> getApplications() {
        return applications;
    }

    /**
     * @param applications
     *            the applications to set
     */
    public void setApplications(List<ApplicationInstance> applications) {
        this.applications = applications;
    }

    /**
     * @return the product
     */
    public ProductRelease getProduct() {
        return product;
    }

    /**
     * @param product
     *            the product to set
     */
    public void setProduct(ProductRelease product) {
        this.product = product;
    }

}
