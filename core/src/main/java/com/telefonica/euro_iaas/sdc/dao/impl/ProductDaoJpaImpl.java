package com.telefonica.euro_iaas.sdc.dao.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.model.Product;
/**
 * JPA implementation for ApplicationDao.
 *
 * @author Sergio Arroyo
 * @version $Id: $
 */
public class ProductDaoJpaImpl extends AbstractBaseDao<Product, String>
        implements ProductDao {

    /** {@inheritDoc} */
    @Override
    public List<Product> findAll() {
        return super.findAll(Product.class);
    }

    /** {@inheritDoc} */
    @Override
    public Product load(String name) throws EntityNotFoundException {
        return super.loadByField(Product.class, "name", name);
    }

}
