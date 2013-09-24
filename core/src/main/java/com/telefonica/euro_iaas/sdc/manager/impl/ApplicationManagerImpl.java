package com.telefonica.euro_iaas.sdc.manager.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ApplicationDao;
import com.telefonica.euro_iaas.sdc.dao.ApplicationReleaseDao;
import com.telefonica.euro_iaas.sdc.manager.ApplicationManager;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationReleaseSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationSearchCriteria;
/**
 * Default ApplicationManager implementation.
 * @author Sergio Arroyo
 *
 */
public class ApplicationManagerImpl implements ApplicationManager {

    private ApplicationDao applicationDao;
    private ApplicationReleaseDao applicationReleaseDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Application> findAll() {
        return applicationDao.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Application> findByCriteria(ApplicationSearchCriteria criteria) {
        return applicationDao.findByCriteria(criteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationRelease load(Application application, String version)
            throws EntityNotFoundException {
        return applicationReleaseDao.load(application, version);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ApplicationRelease> findReleasesByCriteria(
            ApplicationReleaseSearchCriteria criteria) {
        return applicationReleaseDao.findByCriteria(criteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Application load(String name) throws EntityNotFoundException {
        return applicationDao.load(name);
    }

    /**
     * @param productDao the productDao to set
     */
    public void setApplicationDao(ApplicationDao applicationDao) {
        this.applicationDao = applicationDao;
    }

    /**
     * @param applicationReleaseDao the applicationReleaseDao to set
     */
    public void setApplicationReleaseDao(ApplicationReleaseDao applicationReleaseDao) {
        this.applicationReleaseDao = applicationReleaseDao;
    }

}
