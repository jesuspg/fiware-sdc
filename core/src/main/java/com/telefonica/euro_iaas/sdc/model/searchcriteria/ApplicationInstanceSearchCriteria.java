package com.telefonica.euro_iaas.sdc.model.searchcriteria;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.dto.VM;

/**
 * Provides some criteria to search ProductInstance entities.
 * 
 * @author Sergio Arroyo
 */
public class ApplicationInstanceSearchCriteria extends AbstractSearchCriteria {

    /**
     * The status of the application (<i>this criteria return a list of entities<i>).
     */
    private List<Status> status;
    private ProductInstance product;
    private VM vm;
    private String vdc;
    private String applicationName;

    /**
     * Default constructor
     */
    public ApplicationInstanceSearchCriteria() {
    }

    /**
     * @param page
     * @param pagesize
     * @param orderBy
     * @param orderType
     * @param vm
     */
    public ApplicationInstanceSearchCriteria(Integer page, Integer pageSize, String orderBy, String orderType,
            List<Status> status, ProductInstance product, VM vm, String applicationName, String vdc) {
        super(page, pageSize, orderBy, orderType);
        this.status = status;
        this.product = product;
        this.vm = vm;
        this.applicationName = applicationName;
        this.vdc = vdc;
    }

    /**
     * @param orderBy
     * @param orderType
     * @param vm
     */
    public ApplicationInstanceSearchCriteria(String orderBy, String orderType, List<Status> status,
            ProductInstance product, VM vm, String applicationName, String vdc) {
        super(orderBy, orderType);
        this.status = status;
        this.product = product;
        this.vm = vm;
        this.applicationName = applicationName;
        this.vdc = vdc;
    }

    /**
     * @param page
     * @param pagesize
     * @param vm
     */
    public ApplicationInstanceSearchCriteria(Integer page, Integer pageSize, List<Status> status,
            ProductInstance product, VM vm, String applicationName, String vdc) {
        super(page, pageSize);
        this.status = status;
        this.product = product;
        this.vm = vm;
        this.applicationName = applicationName;
        this.vdc = vdc;
    }

    /**
     * @param vm
     */
    public ApplicationInstanceSearchCriteria(List<Status> status, ProductInstance product, VM vm,
            String applicationName, String vdc) {
        this.status = status;
        this.product = product;
        this.vm = vm;
        this.applicationName = applicationName;
        this.vdc = vdc;
    }

    /**
     * @return the status
     */
    public List<Status> getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(List<Status> status) {
        this.status = status;
    }

    /**
     * @return the product
     */
    public ProductInstance getProduct() {
        return product;
    }

    /**
     * @param product
     *            the application to set
     */
    public void setProduct(ProductInstance product) {
        this.product = product;
    }

    /**
     * @return the vm
     */
    public VM getVm() {
        return vm;
    }

    /**
     * @param vm
     *            the vm to set
     */
    public void setVm(VM vm) {
        this.vm = vm;
    }

    /**
     * @return the applicationName
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * @param applicationName
     *            the applicationName to set
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
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

}
