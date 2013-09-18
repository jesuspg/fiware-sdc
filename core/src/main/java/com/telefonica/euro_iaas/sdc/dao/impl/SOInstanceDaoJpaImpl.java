package com.telefonica.euro_iaas.sdc.dao.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.SOInstanceDao;
import com.telefonica.euro_iaas.sdc.model.SOInstance;
/**
 * JPA implementation for SOState.
 *
 * @author Sergio Arroyo
 * @version $Id: $
 */
public class SOInstanceDaoJpaImpl extends AbstractBaseDao<SOInstance, Long>
        implements SOInstanceDao {
    /** {@inheritDoc} */
    @Override
    public List<SOInstance> findAll() {
        return super.findAll(SOInstance.class);
    }

    /** {@inheritDoc} */
    @Override
    public SOInstance load(Long id) throws EntityNotFoundException {
        return super.loadByField(SOInstance.class, "id", id);
    }

}
