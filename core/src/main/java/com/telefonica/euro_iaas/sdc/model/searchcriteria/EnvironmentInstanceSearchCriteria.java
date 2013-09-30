package com.telefonica.euro_iaas.sdc.model.searchcriteria;

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.Environment;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;

/**
 * Provides some criteria to search EnvironmentInstance entities.
 * 
 * @author Jesus M. Movilla
 */
public class EnvironmentInstanceSearchCriteria extends AbstractSearchCriteria {

    /**
     * The environment
     */
    private Environment environment;

    /**
     * The productInstance
     */
    private ProductInstance productInstance;

    /**
     * Default constructor
     */
    public EnvironmentInstanceSearchCriteria() {
    }

    /**
     * @param page
     * @param pagesize
     * @param orderBy
     * @param orderType
     * @param environment
     * @param productInstance
     */
    public EnvironmentInstanceSearchCriteria(Integer page, Integer pageSize, String orderBy, String orderType,
            Environment environment, ProductInstance productInstance) {
        super(page, pageSize, orderBy, orderType);
        this.productInstance = productInstance;
        this.environment = environment;
    }

    /**
     * @param environment
     */
    public EnvironmentInstanceSearchCriteria(Environment environment) {
        this.environment = environment;
    }

    /**
     * @param productInstance
     */
    public EnvironmentInstanceSearchCriteria(ProductInstance productInstance) {
        this.productInstance = productInstance;
    }

    /**
     * @return the environment
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * @param environment
     *            the environment to set
     */
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * @return the productInstance
     */
    public ProductInstance getProductInstance() {
        return productInstance;
    }

    /**
     * @param productInstance
     *            the productInstance to set
     */
    public void setProductInstance(ProductInstance productInstance) {
        this.productInstance = productInstance;
    }

    @Override
    public String toString() {
        return "ProductInstanceSearchCriteria [Environment=" + environment + ", product=" + productInstance + "]";
    }

}
