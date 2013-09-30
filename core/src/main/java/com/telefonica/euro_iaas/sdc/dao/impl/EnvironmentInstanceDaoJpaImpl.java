package com.telefonica.euro_iaas.sdc.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.EnvironmentInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.model.EnvironmentInstance;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.EnvironmentInstanceSearchCriteria;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * JPA implementation for ApplicationInstanceDao.
 * 
 * @author Jesus M. Movilla
 */
public class EnvironmentInstanceDaoJpaImpl extends AbstractInstallableInstanceDaoJpaIml<EnvironmentInstance, Long>
        implements EnvironmentInstanceDao {

    /** {@inheritDoc} */
    @Override
    public List<EnvironmentInstance> findAll() {
        return super.findAll(EnvironmentInstance.class);
    }

    /** {@inheritDoc} */
    @Override
    public EnvironmentInstance load(Long id) throws EntityNotFoundException {
        return super.loadByField(EnvironmentInstance.class, "id", id);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<EnvironmentInstance> findByCriteria(EnvironmentInstanceSearchCriteria criteria) {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria baseCriteria = session.createCriteria(EnvironmentInstance.class);

        if (criteria.getEnvironment() != null) {
            baseCriteria.add(Restrictions.eq("environment", criteria.getEnvironment()));
        }

        List<EnvironmentInstance> applications = setOptionalPagination(criteria, baseCriteria).list();

        // TODO sarroyo: try to do this filter using hibernate criteria.
        if (criteria.getProductInstance() != null) {
            applications = filterByProduct(applications, criteria.getProductInstance());
        }
        return applications;
    }

    @Override
    public EnvironmentInstance findUniqueByCriteria(EnvironmentInstanceSearchCriteria criteria)
            throws NotUniqueResultException {
        List<EnvironmentInstance> environmentInstances = findByCriteria(criteria);
        if (environmentInstances.size() != 1) {
            throw new NotUniqueResultException();
        }
        return environmentInstances.iterator().next();
    }

    /**
     * Filter the result by product instance
     * 
     * @param applications
     * @param product
     * @return
     */
    private List<EnvironmentInstance> filterByProduct(List<EnvironmentInstance> environments,
            ProductInstance productInstance) {
        List<EnvironmentInstance> result = new ArrayList<EnvironmentInstance>();
        for (EnvironmentInstance environment : environments) {
            if (environment.getProductInstances().contains(productInstance)) {
                result.add(environment);
            }
        }
        return result;
    }
}
