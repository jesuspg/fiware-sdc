package com.telefonica.euro_iaas.sdc.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ApplicationInstanceDao;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationInstanceSearchCriteria;

/**
 * JPA implementation for ApplicationInstanceDao.
 *
 * @author Sergio Arroyo
 */
public class ApplicationInstanceDaoJpaImpl extends
        AbstractBaseDao<ApplicationInstance, Long> implements
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
        Criteria baseCriteria = session.createCriteria(
                ApplicationInstance.class);

        if (criteria.getStatus() != null) {
            baseCriteria.add(Restrictions.eq(ApplicationInstance.STATUS_FIELD,
                    criteria.getStatus()));
        }
        return setOptionalPagination(criteria, baseCriteria).list();
    }

}
