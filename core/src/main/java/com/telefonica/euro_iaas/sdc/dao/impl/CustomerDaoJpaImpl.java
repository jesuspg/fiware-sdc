package com.telefonica.euro_iaas.sdc.dao.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.CustomerDao;
import com.telefonica.euro_iaas.sdc.model.Customer;
/**
 * JPA implementation for Customer.
 *
 * @author Sergio Arroyo
 * @version $Id: $
 */
public class CustomerDaoJpaImpl extends AbstractBaseDao<Customer, String>
        implements CustomerDao {

    /** {@inheritDoc} */
    @Override
    public List<Customer> findAll() {
        return super.findAll(Customer.class);
    }

    /** {@inheritDoc} */
    @Override
    public Customer load(String id) throws EntityNotFoundException {
        return super.loadByField(Customer.class, "name", id);
    }

}
