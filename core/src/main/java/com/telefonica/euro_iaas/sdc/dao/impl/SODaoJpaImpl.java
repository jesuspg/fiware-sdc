package com.telefonica.euro_iaas.sdc.dao.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.SODao;
import com.telefonica.euro_iaas.sdc.model.SO;
/**
 * JPA implementation for SO.
 *
 * @author Sergio Arroyo
 * @version $Id: $
 */
public class SODaoJpaImpl extends AbstractBaseDao<SO, String> implements SODao {

    /** {@inheritDoc} */
    @Override
    public List<SO> findAll() {
        return super.findAll(SO.class);
    }

    /** {@inheritDoc} */
    @Override
    public SO load(String name) throws EntityNotFoundException {
        return super.loadByField(SO.class, "name", name);
    }


}
