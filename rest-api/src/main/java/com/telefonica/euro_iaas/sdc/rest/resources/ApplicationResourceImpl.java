package com.telefonica.euro_iaas.sdc.rest.resources;

import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.manager.ApplicationManager;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationReleaseSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationSearchCriteria;

/**
 * default ApplicationResource implementation
 * @author Sergio Arroyo
 *
 */
@Path("/catalog/application")
@Component
@Scope("request")
public class ApplicationResourceImpl implements ApplicationResource {

    @InjectParam("applicationManager")
    private ApplicationManager applicationManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Application> findAll(Integer page, Integer pageSize,
            String orderBy, String orderType) {
        ApplicationSearchCriteria criteria = new ApplicationSearchCriteria();
        if (page != null && pageSize != null) {
            criteria.setPage(page);
            criteria.setPageSize(pageSize);
        }
        if (!StringUtils.isEmpty(orderBy)) {
            criteria.setOrderBy(orderBy);
        }
        if (!StringUtils.isEmpty(orderType)) {
            criteria.setOrderBy(orderType);
        }
        return applicationManager.findByCriteria(criteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Application load(String name) throws EntityNotFoundException {
        return applicationManager.load(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Attribute> loadAttributes(String name)
            throws EntityNotFoundException {
        return applicationManager.load(name).getAttributes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ApplicationRelease> findAll(String name, Integer page,
            Integer pageSize, String orderBy, String orderType) {
        ApplicationReleaseSearchCriteria criteria =
            new ApplicationReleaseSearchCriteria();

        if (!StringUtils.isEmpty(name)) {
            try {
                Application application = applicationManager.load(name);
                criteria.setApplication(application);
            } catch (EntityNotFoundException e) {
                throw new SdcRuntimeException(
                        "Can not find the application " + name, e);
            }
        }

        if (page != null && pageSize != null) {
            criteria.setPage(page);
            criteria.setPageSize(pageSize);
        }
        if (!StringUtils.isEmpty(orderBy)) {
            criteria.setOrderBy(orderBy);
        }
        if (!StringUtils.isEmpty(orderType)) {
            criteria.setOrderBy(orderType);
        }
        return applicationManager.findReleasesByCriteria(criteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationRelease load(String name, String version)
            throws EntityNotFoundException {
        Application application = applicationManager.load(name);
        return applicationManager.load(application, version);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Attribute> loadAttributes(String name, String version)
            throws EntityNotFoundException {
        return load(name, version).getAttributes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ApplicationRelease> findTransitable(String name, String version)
            throws EntityNotFoundException {
        return load(name, version).getTransitableReleases();
    }

}
