package com.telefonica.euro_iaas.sdc.manager;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationReleaseSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationSearchCriteria;

/**
 * Defines the operations the system shall be able to do with Applications.
 *
 * @author Sergio Arroyo
 *
 */
public interface ApplicationManager {

    /**
     * Find the application using the given name.
     * @param name the application identifier
     * @return the application.
     * @throws EntityNotFoundException if the application does not exists
     */
    Application load(String name) throws EntityNotFoundException;

    /**
     * Retrieve all applications created in the system.
     * @return the existent application.
     */
    List<Application> findAll();

    /**
     * Find the applications that match with the given criteria.
     * @param criteria the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<Application> findByCriteria(ApplicationSearchCriteria criteria);

    /**
     * Retrieve an application release for a given applicaiton and version.
     * @param application the application
     * @param version the version
     * @return the product release that match with the criteria
     * @throws EntityNotFoundException if the product release does not exists
     */
    ApplicationRelease load(Application application, String version)
        throws EntityNotFoundException;

    /**
     * Find all application releases that match with the given criteria.
     * @param criteria the search criteria
     * @return the application releases.
     */
    List<ApplicationRelease> findReleasesByCriteria(
            ApplicationReleaseSearchCriteria criteria);
}
