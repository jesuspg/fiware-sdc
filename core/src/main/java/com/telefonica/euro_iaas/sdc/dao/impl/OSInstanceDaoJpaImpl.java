package com.telefonica.euro_iaas.sdc.dao.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.OSInstanceDao;
import com.telefonica.euro_iaas.sdc.model.OSInstance;
/**
 * JPA implementation for SOState.
 *
 * @author Sergio Arroyo
 * @version $Id: $
 */
public class OSInstanceDaoJpaImpl extends AbstractBaseDao<OSInstance, Long>
        implements OSInstanceDao {
    /** {@inheritDoc} */
    @Override
    public List<OSInstance> findAll() {
        return super.findAll(OSInstance.class);
    }

    /** {@inheritDoc} */
    @Override
    public OSInstance load(Long id) throws EntityNotFoundException {
        return super.loadByField(OSInstance.class, "id", id);
    }

}
