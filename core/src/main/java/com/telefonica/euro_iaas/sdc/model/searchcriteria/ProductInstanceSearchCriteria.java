package com.telefonica.euro_iaas.sdc.model.searchcriteria;

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.VM;

/**
 * Provides some criteria to search ProductInstance entities.
 *
 * @author Sergio Arroyo
 *
 */
public class ProductInstanceSearchCriteria extends AbstractSearchCriteria {

    /**
     * The host where the product is installed (<i>this criteria return a list
     * of entities<i>).
     */
    private VM vm;

    /**
     * The status of the application (<i>this criteria return a list
     * of entities<i>).
     */
    private Status status;

    /**
     * The product.
     */
    private ProductRelease product;

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
    public ProductInstanceSearchCriteria(Integer page, Integer pageSize,
            String orderBy, String orderType, VM vm, Status status,
            ProductRelease product) {
        super(page, pageSize, orderBy, orderType);
        this.vm = vm;
        this.status = status;
        this.product = product;
    }

    /**
     * @param orderBy
     * @param orderType
     * @param vm
     * @param product
     */
    public ProductInstanceSearchCriteria(String orderBy, String orderType,
            VM vm, Status status, ProductRelease product) {
        super(orderBy, orderType);
        this.vm = vm;
        this.status = status;
        this.product = product;
    }

    /**
     * @param page
     * @param pagesize
     * @param vm
     * @param product
     */
    public ProductInstanceSearchCriteria(Integer page, Integer pageSize,
            VM vm, Status status, ProductRelease product) {
        super(page, pageSize);
        this.vm = vm;
        this.status = status;
        this.product = product;
    }

    /**
     * @param vm
     */
    public ProductInstanceSearchCriteria(
            VM vm, Status status, ProductRelease product) {
        this.vm = vm;
        this.status = status;
        this.product = product;
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
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
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
    public ProductRelease getProduct() {
        return product;
    }

    /**
     * @param vm the vm to set
     */
    public void setVm(VM vm) {
        this.vm = vm;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(ProductRelease product) {
        this.product = product;
    }


    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "ProductInstanceSearchCriteria [vm=" + vm + ", status=" + status
                + ", product=" + product + ", productName=" + productName + "]";
    }


}
