package com.telefonica.euro_iaas.sdc.model.searchcriteria;

import com.telefonica.euro_iaas.sdc.model.Customer;
/**
 * Search criteria for SO entities.
 *
 * @author Sergio Arroyo
 * @version $Id: $
 */
public class SOSearchCriteria {

    /** */
    private Customer customer;


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
}

