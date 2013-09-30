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
