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

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.fiware.commons.dao.AbstractBaseDao;

public abstract class AbstractInstallableInstanceDaoJpaIml<T, Id extends Serializable> extends AbstractBaseDao<T, Id> {

    public Criterion getVMCriteria(Criteria baseCriteria, VM vm) {
        if (!StringUtils.isEmpty(vm.getFqn()) && !StringUtils.isEmpty(vm.getIp())
                && !StringUtils.isEmpty(vm.getDomain()) && !StringUtils.isEmpty(vm.getHostname())) {
            return Restrictions.eq(InstallableInstance.VM_FIELD, vm);
        } else if (!StringUtils.isEmpty(vm.getFqn())) {
            return Restrictions.eq("vm.fqn", vm.getFqn());
        } else if (!StringUtils.isEmpty(vm.getIp())) {
            return Restrictions.eq("vm.ip", vm.getIp());
        } else if (!StringUtils.isEmpty(vm.getDomain()) && !StringUtils.isEmpty(vm.getHostname())) {
            return Restrictions.and(Restrictions.eq("vm.hostname", vm.getHostname()),
                    Restrictions.eq("vm.domain", vm.getDomain()));
        } else {
            throw new SdcRuntimeException("Invalid VM while finding products by criteria");
        }
    }
}
