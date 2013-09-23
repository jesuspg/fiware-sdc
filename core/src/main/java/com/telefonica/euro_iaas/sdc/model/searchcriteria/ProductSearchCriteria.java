package com.telefonica.euro_iaas.sdc.model.searchcriteria;

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.Customer;
import com.telefonica.euro_iaas.sdc.model.OS;
/**
 * Search criteria for products entities.
 *
 * @author Sergio Arroyo
 * @version $Id: $
 */
public class ProductSearchCriteria extends AbstractSearchCriteria {

    /** */
    private Customer customer;
    /** */
    private OS so;


    /**
     * @param page
     * @param pagesize
     * @param orderBy
     * @param orderType
     */
    public ProductSearchCriteria(Integer page, Integer pageSize,
            String orderBy, String orderType) {
        super(page, pageSize, orderBy, orderType);
    }

    /**
     * @param orderBy
     * @param orderType
     */
    public ProductSearchCriteria(String orderBy, String orderType) {
        super(orderBy, orderType);
    }

    /**
     * @param page
     * @param pagesize
     */
    public ProductSearchCriteria(Integer page, Integer pageSize) {
        super(page, pageSize);
    }

    /**
     */
    public ProductSearchCriteria() {
        super();
    }

    /**
     * <p>Getter for the field <code>customer</code>.</p>
     *
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }
    /**
     * <p>Setter for the field <code>customer</code>.</p>
     *
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    /**
     * <p>Getter for the field <code>so</code>.</p>
     *
     * @return the so
     */
    public OS getSo() {
        return so;
    }
    /**
     * <p>Setter for the field <code>so</code>.</p>
     *
     * @param so the so to set
     */
    public void setSo(OS so) {
        this.so = so;
    }
}

