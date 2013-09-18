package com.telefonica.euro_iaas.sdc.model.searchcriteria;

import com.telefonica.euro_iaas.sdc.model.Customer;
import com.telefonica.euro_iaas.sdc.model.SO;
/**
 * Search criteria for application entities.
 *
 * @author Sergio Arroyo
 * @version $Id: $
 */
public class ProductSearchCriteria {

    /** */
    private Customer customer;
    /** */
    private SO so;


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
    public SO getSo() {
        return so;
    }
    /**
     * <p>Setter for the field <code>so</code>.</p>
     *
     * @param so the so to set
     */
    public void setSo(SO so) {
        this.so = so;
    }
}

