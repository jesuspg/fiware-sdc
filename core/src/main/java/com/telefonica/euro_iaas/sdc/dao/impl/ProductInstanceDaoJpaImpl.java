package com.telefonica.euro_iaas.sdc.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;
/**
 * JPA implementation for AppState.
 *
 * @author Sergio Arroyo
 *
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


    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ProductInstance> findByCriteria(
            ProductInstanceSearchCriteria criteria) {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria baseCriteria = session.createCriteria(ProductInstance.class);

        if (criteria.getVM() != null) {
            baseCriteria.add(getVMCriteria(baseCriteria, criteria.getVm()));
        }
        if (criteria.getStatus() != null) {
            baseCriteria.add(Restrictions.eq(ProductInstance.STATUS_FIELD,
                    criteria.getStatus()));
        }
        if (criteria.getProduct() != null) {
            baseCriteria.add(Restrictions.eq(ProductInstance.PRODUCT_FIELD,
                    criteria.getProduct()));
        }

        if (!StringUtils.isEmpty(criteria.getProductName())) {
            baseCriteria.createAlias("product", "rls")
            .createAlias("rls.product", "prod");
            baseCriteria.add(Restrictions.eq("prod.name",
                    criteria.getProductName()));
        }
        return setOptionalPagination(criteria, baseCriteria).list();
    }

    @Override
    public ProductInstance findUniqueByCriteria(
            ProductInstanceSearchCriteria criteria)
                    throws NotUniqueResultException {
        List<ProductInstance> instances = findByCriteria(criteria);
        if (instances.size() != 1) {
            throw new NotUniqueResultException();
        }
        return findByCriteria(criteria).iterator().next();
    }

    public Criterion getVMCriteria(Criteria baseCriteria, VM vm) {
        if (!StringUtils.isEmpty(vm.getIp()) && !StringUtils.isEmpty(vm.getDomain())
                && !StringUtils.isEmpty(vm.getHostname())) {
            return Restrictions.eq(ProductInstance.VM_FIELD,
                    vm);
        } else if (!StringUtils.isEmpty(vm.getDomain())
                && !StringUtils.isEmpty(vm.getHostname())) {
            return Restrictions.and(
                    Restrictions.eq("vm.hostname", vm.getHostname()),
                    Restrictions.eq("vm.domain", vm.getDomain()));
        } else if (!StringUtils.isEmpty(vm.getIp())) {
            return Restrictions.eq("vm.ip", vm.getIp());
        } else {
            throw new SdcRuntimeException(
                    "Invalid VM while finding products by criteria");
        }

    }

}
