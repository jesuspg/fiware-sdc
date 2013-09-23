package com.telefonica.euro_iaas.sdc.dao.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
/**
 * JPA implementation for AppState.
 *
 * @author Sergio Arroyo
 * @version $Id: $
 */
public class ProductInstanceDaoJpaImpl
        extends AbstractBaseDao<ProductInstance, Long>
        implements ProductInstanceDao {

    /** {@inheritDoc} */
    @Override
    public List<ProductInstance> findAll() {
        return super.findAll(ProductInstance.class);
    }

    /** {@inheritDoc} */
    @Override
    public ProductInstance load(Long id) throws EntityNotFoundException {
        return super.loadByField(ProductInstance.class, "id", id);
    }

}
