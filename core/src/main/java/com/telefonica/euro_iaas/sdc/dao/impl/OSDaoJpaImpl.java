package com.telefonica.euro_iaas.sdc.dao.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.OSDao;
import com.telefonica.euro_iaas.sdc.model.OS;
/**
 * JPA implementation for SO.
 *
 * @author Sergio Arroyo
 * @version $Id: $
 */
public class OSDaoJpaImpl extends AbstractBaseDao<OS, String> implements OSDao {

    /** {@inheritDoc} */
    @Override
    public List<OS> findAll() {
        return super.findAll(OS.class);
    }

    /** {@inheritDoc} */
    @Override
    public OS load(String osType) throws EntityNotFoundException {
        return super.loadByField(OS.class, "osType", osType);
    }


}
