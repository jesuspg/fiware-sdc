package com.telefonica.euro_iaas.sdc.dao.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ApplicationInstanceDao;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;

/**
 * JPA implementation for ApplicationInstanceDao.
 *
 * @author Sergio Arroyo
 * @version $Id: $
 */
public class ApplicationInstanceDaoJpaImpl extends
        AbstractBaseDao<ApplicationInstance, Long> implements
        ApplicationInstanceDao {

    /** {@inheritDoc} */
    @Override
    public List<ApplicationInstance> findAll() {
        return super.findAll(ApplicationInstance.class);
    }

    /** {@inheritDoc} */
    @Override
    public ApplicationInstance load(Long id) throws EntityNotFoundException {
        return super.loadByField(ApplicationInstance.class, "id", id);
    }

}
