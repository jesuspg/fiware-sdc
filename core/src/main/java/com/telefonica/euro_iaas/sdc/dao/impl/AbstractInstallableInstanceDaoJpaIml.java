package com.telefonica.euro_iaas.sdc.dao.impl;

import java.io.Serializable;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

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
