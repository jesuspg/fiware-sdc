package com.telefonica.euro_iaas.sdc.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ApplicationInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationInstanceSearchCriteria;

/**
 * JPA implementation for ApplicationInstanceDao.
 *
 * @author Sergio Arroyo
 */
public class ApplicationInstanceDaoJpaImpl extends
AbstractInstallableInstanceDaoJpaIml<ApplicationInstance, Long> implements
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

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ApplicationInstance> findByCriteria(
            ApplicationInstanceSearchCriteria criteria) {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria baseCriteria = session
                .createCriteria(ApplicationInstance.class);

        if (criteria.getStatus() != null && !criteria.getStatus().isEmpty()) {
            Criterion statusCr = null;
            for (Status status : criteria.getStatus()) {
                statusCr = addStatus(statusCr, status);
            }
            baseCriteria.add(statusCr);
        }
        if (!StringUtils.isEmpty(criteria.getApplicationName())) {
            baseCriteria.createAlias("application", "rls")
            .createAlias("rls.application", "app");
            baseCriteria.add(Restrictions.eq("app.name",
                    criteria.getApplicationName()));
        }
        if (!StringUtils.isEmpty(criteria.getVdc())) {
            baseCriteria.add(Restrictions.eq(ApplicationInstance.VDC_FIELD,
                    criteria.getVdc()));
        }
        if (criteria.getVm() != null) {
            baseCriteria.add(getVMCriteria(baseCriteria, criteria.getVm()));
        }
        List<ApplicationInstance> applications = setOptionalPagination(
                criteria, baseCriteria).list();
        // TODO sarroyo: try to do this filter using hibernate criteria.
        if (criteria.getProduct() != null) {
            applications = filterByProduct(applications, criteria.getProduct());
        }
        return applications;
    }

    @Override
    public ApplicationInstance findUniqueByCriteria(
            ApplicationInstanceSearchCriteria criteria)
                    throws NotUniqueResultException {
        List<ApplicationInstance> instances = findByCriteria(criteria);
        if (instances.size() != 1) {
            throw new NotUniqueResultException();
        }
        return instances.iterator().next();
    }

    /**
     * Filter the result by product instance
     *
     * @param applications
     * @param product
     * @return
     */
    private List<ApplicationInstance> filterByProduct(
            List<ApplicationInstance> applications, ProductInstance product) {
        List<ApplicationInstance> result = new ArrayList<ApplicationInstance>();
        for (ApplicationInstance application : applications) {
            if (application.getEnvironmentInstance().getProductInstances().contains(product)) {
                result.add(application);
            }
        }
        return result;
    }


    private Criterion addStatus(Criterion statusCr, Status status) {
        SimpleExpression expression = Restrictions.eq("status", status);
        if (statusCr == null) {
            statusCr = expression;
        } else {
            statusCr = Restrictions.or(statusCr, expression);
        }
        return statusCr;
    }
}
