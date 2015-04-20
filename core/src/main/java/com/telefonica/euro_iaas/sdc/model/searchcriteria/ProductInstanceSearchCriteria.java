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

import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.fiware.commons.dao.AbstractSearchCriteria;

/**
 * Provides some criteria to search ProductInstance entities.
 * 
 * @author Sergio Arroyo
 */
public class ProductInstanceSearchCriteria extends AbstractSearchCriteria {

    /**
     * The host where the product is installed (<i>this criteria return a list of entities<i>).
     */
    private VM vm;

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
    private ProductRelease productRelease;

    private String productName;

    /**
     * Default constructor
     */
    public ProductInstanceSearchCriteria() {
    }

    /**
     * @param page
     * @param pagesize
     * @param orderBy
     * @param orderType
     * @param vm
     * @param product
     */
    public ProductInstanceSearchCriteria(Integer page, Integer pageSize, String orderBy, String orderType, VM vm,
            Status status, ProductRelease productRelease, String vdc) {
        super(page, pageSize, orderBy, orderType);
        this.vm = vm;
        this.status = status;
        this.productRelease = productRelease;
        this.vdc = vdc;
    }

    /**
     * @param orderBy
     * @param orderType
     * @param vm
     * @param product
     */
    public ProductInstanceSearchCriteria(String orderBy, String orderType, VM vm, Status status,
            ProductRelease productRelease, String vdc) {
        super(orderBy, orderType);
        this.vm = vm;
        this.status = status;
        this.productRelease = productRelease;
        this.vdc = vdc;
    }

    /**
     * @param page
     * @param pagesize
     * @param vm
     * @param product
     */
    public ProductInstanceSearchCriteria(Integer page, Integer pageSize, VM vm, Status status,
            ProductRelease productRelease, String vdc) {
        super(page, pageSize);
        this.vm = vm;
        this.status = status;
        this.productRelease = productRelease;
        this.vdc = vdc;
    }

    /**
     * @param vm
     */
    public ProductInstanceSearchCriteria(VM vm, Status status, ProductRelease productRelease, String vdc) {
        this.vm = vm;
        this.status = status;
        this.productRelease = productRelease;
        this.vdc = vdc;
    }

    /**
     * @return the host
     */
    public VM getVM() {
        return vm;
    }

    /**
     * @param vm
     *            the host to set
     */
    public void setVM(VM vm) {
        this.vm = vm;
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
     * @return the vm
     */
    public VM getVm() {
        return vm;
    }

    /**
     * @return the product
     */
    public ProductRelease getProductRelease() {
        return productRelease;
    }

    /**
     * @param vm
     *            the vm to set
     */
    public void setVm(VM vm) {
        this.vm = vm;
    }

    /**
     * @param product
     *            the product to set
     */
    public void setProductRelease(ProductRelease productRelease) {
        this.productRelease = productRelease;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName
     *            the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "ProductInstanceSearchCriteria [vm=" + vm + ", status=" + status + ", product=" + productRelease
                + ", productName=" + productName + "]";
    }

}
