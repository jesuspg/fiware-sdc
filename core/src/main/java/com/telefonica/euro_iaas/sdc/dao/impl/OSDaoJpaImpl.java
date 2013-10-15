/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

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
