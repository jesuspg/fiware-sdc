/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.sdc.dao.impl;

import java.util.List;

import com.telefonica.euro_iaas.sdc.dao.OSDao;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.fiware.commons.dao.AbstractBaseDao;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;

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

    public OS load(Long id) throws EntityNotFoundException {
        return super.loadByField(OS.class, "id", id);
    }

}
