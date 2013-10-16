/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.model.searchcriteria;

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;

/**
 * Provides some criteria to search ProductInstance entities.
 * 
 * @author Sergio Arroyo
 */
public class ArtifactSearchCriteria extends AbstractSearchCriteria {

    /**
     * The vdc where the product is installed.
     */
    private String vdc;
    /**
     * The status of the application (<i>this criteria return a list of entities<i>).
     */
    private Status status;

    /**
     * The product.
     */
    private ProductInstance productInstance;

    private String artifactName;

    /**
     * Default constructor
     */
    public ArtifactSearchCriteria() {
    }

    /**
     * @param page
     * @param pagesize
     * @param orderBy
     * @param orderType
     * @param productInstance
     */
    public ArtifactSearchCriteria(Integer page, Integer pageSize, String orderBy, String orderType, Status status,
            ProductInstance productInstance, String vdc) {
        super(page, pageSize, orderBy, orderType);
        this.status = status;
        this.productInstance = productInstance;
        this.vdc = vdc;
    }

    /**
     * @param orderBy
     * @param orderType
     * @param vm
     * @param product
     */
    public ArtifactSearchCriteria(String orderBy, String orderType, Status status, ProductInstance productInstance,
            String vdc) {
        super(orderBy, orderType);
        this.status = status;
        this.productInstance = productInstance;
        this.vdc = vdc;
    }

    /**
     * @param orderBy
     * @param orderType
     * @param vm
     * @param product
     */
    public ArtifactSearchCriteria(String orderBy, String orderType, ProductInstance productInstance, String vdc) {
        super(orderBy, orderType);
        this.productInstance = productInstance;
        this.vdc = vdc;
    }

    /**
     * @return the vdc
     */
    public String getVdc() {
        return vdc;
    }

    /**
     * @param vdc
     *            the vdc to set
     */
    public void setVdc(String vdc) {
        this.vdc = vdc;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * @return the product
     */
    public ProductInstance getProductInstance() {
        return productInstance;
    }

    /**
     * @param product
     *            the product to set
     */
    public void setProductInstance(ProductInstance productInstance) {
        this.productInstance = productInstance;
    }

    /**
     * @return the productName
     */
    public String getArtifactName() {
        return artifactName;
    }

    /**
     * @param productName
     *            the productName to set
     */
    public void setArtifactName(String productName) {
        this.artifactName = artifactName;
    }

    @Override
    public String toString() {
        return "ArtifactSearchCriteria [ status=" + status + ", product=" + productInstance + ", artifactName="
                + artifactName + "]";
    }

}
