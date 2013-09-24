package com.telefonica.euro_iaas.sdc.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ApplicationReleaseDao;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationReleaseSearchCriteria;
/**
 * JPA based  implementation for ApplicationReleaseDao.
 *
 * @author Sergio Arroyo
 *
 */
public class ApplicationReleaseDaoJpaImpl
    extends AbstractBaseDao<ApplicationRelease, Long>
    implements ApplicationReleaseDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ApplicationRelease> findAll() {
        return super.findAll(ApplicationRelease.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationRelease load(Long arg0) throws EntityNotFoundException {
        return super.loadByField(ApplicationRelease.class, "id", arg0);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ApplicationRelease> findByCriteria(
            ApplicationReleaseSearchCriteria criteria) {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria baseCriteria = session.createCriteria(
                ApplicationRelease.class);
        if (criteria.getApplication() != null) {
            baseCriteria.add(Restrictions.eq(
                    "application", criteria.getApplication()));
        }
        return setOptionalPagination(criteria, baseCriteria).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationRelease load(Application application, String version)
            throws EntityNotFoundException {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria baseCriteria = session.createCriteria(
                ApplicationRelease.class);
        baseCriteria.add(Restrictions.eq("application", application));
        baseCriteria.add(Restrictions.eq("version", version));

        ApplicationRelease release = (ApplicationRelease) baseCriteria.uniqueResult();
        if (release == null) {
            String[] keys = {"application", "version"};
            Object[] values = {application, version};
            throw new EntityNotFoundException(ApplicationRelease.class,
                    keys, values);
        }
        return release;
    }

}
