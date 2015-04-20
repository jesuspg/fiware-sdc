/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.sdc.model.searchcriteria;

import com.telefonica.fiware.commons.dao.AbstractSearchCriteria;
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
