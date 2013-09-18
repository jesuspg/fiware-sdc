package com.telefonica.euro_iaas.sdc.exception;

import java.util.List;

import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;

/**
 * Exception thrown when try to uninstall a product that has any application
 * running on it.
 *
 * @author Sergio Arroyo
 */
@SuppressWarnings("serial")
public class ApplicationInstalledException extends Exception {

    private List<ApplicationInstance> applications;
    private ProductInstance product;

    public ApplicationInstalledException() {
        super();
    }


    /**
     * @param applications
     * @param product
     */
    public ApplicationInstalledException(
            List<ApplicationInstance> applications, ProductInstance product) {
        super("The product " + product.getProduct().getProduct().getName() +
                "has " + applications.size() + " applications installed on it.");
        this.applications = applications;
        this.product = product;
    }


    public ApplicationInstalledException(String msg) {
        super(msg);
    }

    public ApplicationInstalledException(Throwable e) {
        super(e);
    }

    public ApplicationInstalledException(String msg, Throwable e) {
        super(msg, e);
    }


    /**
     * @return the applications
     */
    public List<ApplicationInstance> getApplications() {
        return applications;
    }


    /**
     * @param applications the applications to set
     */
    public void setApplications(List<ApplicationInstance> applications) {
        this.applications = applications;
    }


    /**
     * @return the product
     */
    public ProductInstance getProduct() {
        return product;
    }


    /**
     * @param product the product to set
     */
    public void setProduct(ProductInstance product) {
        this.product = product;
    }

}
