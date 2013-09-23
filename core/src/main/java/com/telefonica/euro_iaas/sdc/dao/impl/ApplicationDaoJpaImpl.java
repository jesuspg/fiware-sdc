package com.telefonica.euro_iaas.sdc.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ApplicationDao;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationSearchCriteria;
/**
 * JPA implementation for ApplicationDao.
 *
 * @author Sergio Arroyo
 * @version $Id: $
 */
public class ApplicationDaoJpaImpl extends AbstractBaseDao<Application, String>
        implements ApplicationDao {

    /** {@inheritDoc} */
    @Override
    public List<Application> findAll() {
        return super.findAll(Application.class);
    }

    /** {@inheritDoc} */
    @Override
    public Application load(String id) throws EntityNotFoundException {
        return super.loadByField(Application.class, "name", id);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Application> findByCriteria(ApplicationSearchCriteria criteria) {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria baseCriteria = session.createCriteria(Application.class);
        return setOptionalPagination(criteria, baseCriteria).list();
    }

}
