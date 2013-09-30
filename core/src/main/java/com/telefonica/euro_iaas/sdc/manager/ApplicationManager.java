/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */

package com.telefonica.euro_iaas.sdc.manager;

import java.io.File;
import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsApplicationReleaseException;
import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.ApplicationReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ApplicationReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.exception.EnvironmentNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.InvalidApplicationReleaseException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationReleaseSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationSearchCriteria;

/**
 * Defines the operations the system shall be able to do with Applications.
 * 
 * @author Sergio Arroyo
 */
public interface ApplicationManager {

    /**
     * Find the application using the given name.
     * 
     * @param name
     *            the application identifier
     * @return the application.
     * @throws EntityNotFoundException
     *             if the application does not exists
     */
    Application load(String name) throws EntityNotFoundException;

    /**
     * Retrieve all applications created in the system.
     * 
     * @return the existent application.
     */
    List<Application> findAll();

    /**
     * Find the applications that match with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<Application> findByCriteria(ApplicationSearchCriteria criteria);

    /**
     * Retrieve an application release for a given applicaiton and version.
     * 
     * @param application
     *            the application
     * @param version
     *            the version
     * @return the product release that match with the criteria
     * @throws EntityNotFoundException
     *             if the product release does not exists
     */
    ApplicationRelease load(Application application, String version) throws EntityNotFoundException;

    /**
     * Find all application releases that match with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the application releases.
     */
    List<ApplicationRelease> findReleasesByCriteria(ApplicationReleaseSearchCriteria criteria);

    /**
     * Insert the Application Release consisting on.
     * 
     * @param ApplicationRelease
     *            Object with name, version, transitableUploads..
     * @param The
     *            recipes to be uploaded to the chef server in a tar file
     * @param The
     *            scripts/packages required to install/uninstall/configure the product
     * @throws AlreadyExistsProductReleaseException
     * @throws InvalidProductReleaseException
     * @throws ProductReleaseNotFoundException
     * @throws EnvironmentNotFoundException
     */
    ApplicationRelease insert(ApplicationRelease appRelase, File recipes, File installable)
            throws AlreadyExistsApplicationReleaseException, InvalidApplicationReleaseException,
            ProductReleaseNotFoundException, InvalidProductReleaseException, EnvironmentNotFoundException;

    /**
     * Delete the Application Release consisting on.
     * 
     * @param ApplicationRelease
     *            Object with name, version, transitableUploads..
     * @throws ProductReleaseNotFoundException
     * @throws ProductReleaseStillInstalledException
     */
    void delete(ApplicationRelease applicationRelase) throws ApplicationReleaseNotFoundException,
            ApplicationReleaseStillInstalledException;

    /**
     * Update the Application Release (either the cookbook/installable)
     * 
     * @param type
     *            "cookbook" or "installable"
     * @param file
     *            to be uploaded
     * @return applicationRelease
     * @throws ApplicationReleaseNotFoundException
     * @throws InvalidApplicationReleaseException
     * @throws ProductReleaseNotFoundException
     */
    ApplicationRelease update(ApplicationRelease appRelease, File recipes, File installable)
            throws ApplicationReleaseNotFoundException, InvalidApplicationReleaseException,
            ProductReleaseNotFoundException, InvalidProductReleaseException, EnvironmentNotFoundException;

}
