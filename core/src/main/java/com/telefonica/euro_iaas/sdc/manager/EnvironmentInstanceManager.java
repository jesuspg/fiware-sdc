package com.telefonica.euro_iaas.sdc.manager;

import java.util.List;

import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsEnvironmentInstanceException;
import com.telefonica.euro_iaas.sdc.exception.ApplicationInstanceStillInstalledException;
import com.telefonica.euro_iaas.sdc.exception.EnvironmentInstanceNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.EnvironmentNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.InvalidEnvironmentInstanceException;
import com.telefonica.euro_iaas.sdc.exception.ProductInstanceNotFoundException;
import com.telefonica.euro_iaas.sdc.model.EnvironmentInstance;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.EnvironmentInstanceSearchCriteria;

/**
 * Defines the operations the system shall be able to do with Environment Instance.
 * 
 * @author Jesus M. Movilla
 */
public interface EnvironmentInstanceManager {

    /**
     * Find the application using the given name.
     * 
     * @param id
     *            the environment instance identifier
     * @return the EnvironmentInstance.
     * @throws EntityNotFoundException
     *             if the application does not exists
     */
    EnvironmentInstance load(Long id) throws EnvironmentInstanceNotFoundException;

    /**
     * Retrieve all environment instances created in the system.
     * 
     * @return the existent environment instances.
     */
    List<EnvironmentInstance> findAll();

    /**
     * Find the EnvironmentInstance that match with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<EnvironmentInstance> findByCriteria(EnvironmentInstanceSearchCriteria criteria);

    /**
     * Insert the Application Release consisting on.
     * 
     * @param ApplicationRelease
     *            Object with name, version, transitableUploads..
     * @param The
     *            recipes to be uploaded to the chef server in a tar file
     * @param The
     *            scripts/packages required to install/uninstall/configure the product
     * @throws EnvironmentNotFoundException
     * @throws ProductInstanceNotFoundException
     */
    EnvironmentInstance insert(EnvironmentInstance environmentInstance) throws EnvironmentNotFoundException,
            ProductInstanceNotFoundException, InvalidEnvironmentInstanceException,
            AlreadyExistsEnvironmentInstanceException;

    /**
     * Delete the EnvironmentInstance consisting on.
     * 
     * @param EnvironmentInstance
     *            identifier
     * @throws EnvironmentInstanceNotFoundException
     * @throws ApplicationInstanceStillInstalledException
     */
    void delete(Long Id) throws EnvironmentInstanceNotFoundException, ApplicationInstanceStillInstalledException;

    /**
     * Update the EnvironmentInstance
     * 
     * @param EnvironmentInstance
     * @return applicationRelease
     * @throws EnvironmentInstanceNotFoundException
     * @throws ProductInstanceNotFoundException
     * @throws EnvironmentNotFoundException
     * @throws InvalidEnvironmentInstanceException
     */
    EnvironmentInstance update(EnvironmentInstance environmentInstance) throws EnvironmentInstanceNotFoundException,
            EnvironmentNotFoundException, ProductInstanceNotFoundException, InvalidEnvironmentInstanceException;

}
