package com.telefonica.euro_iaas.sdc.manager;

import java.util.List;

import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsEnvironmentException;
import com.telefonica.euro_iaas.sdc.exception.EnvironmentNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.InvalidEnvironmentException;
import com.telefonica.euro_iaas.sdc.exception.ProductNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseInApplicationReleaseException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.model.Environment;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.EnvironmentSearchCriteria;

public interface EnvironmentManager {

    /**
     * Retrieve all Environment created in the system.
     * 
     * @return the existent Environment.
     */
    List<Environment> findAll();

    /**
     * Find the environments that match with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<Environment> findByCriteria(EnvironmentSearchCriteria criteria);

    /**
     * Retrieve a Environment for a given name.
     * 
     * @param Environment
     *            nanme
     * @return the environment that match with the criteria
     * @throws EnvironmentNotFoundException
     *             if the product release does not exists
     */
    Environment load(String name) throws EnvironmentNotFoundException;

    /**
     * Insert the Environment consisting on.
     * 
     * @param Environment
     *            Object with name, version, transitableUploads..
     * @throws AlreadyExistsEnvironmentException
     * @throws InvalidEnvironmentException
     * @throws ProductReleaseNotFoundException
     * @return the products releases.
     */
    Environment insert(Environment environment) throws ProductNotFoundException, AlreadyExistsEnvironmentException,
            InvalidEnvironmentException, ProductReleaseNotFoundException;

    /**
     * Delete the Environment consisting on.
     * 
     * @param Environment
     *            Object with List<ProductRelease>
     * @throws EnvironmentNotFoundException
     *             thrown when try
     * @throws ProductReleaseStillInstalledException
     * @throws ProductReleaseInApplicationReleaseException
     * @return void.
     */
    void delete(String name) throws EnvironmentNotFoundException, ProductReleaseStillInstalledException,
            ProductReleaseInApplicationReleaseException;

    /**
     * Update the Environment
     * 
     * @param List
     *            <ProductRelease>
     * @return Environment
     * @throws EnvironmentNotFoundException
     * @throws AlreadyExistsEnvironmentException
     * @throws InvalidEnvironmentException
     * @throws ProductReleaseNotFoundException
     * @throws ProductNotFoundException
     */
    Environment update(Environment environment) throws EnvironmentNotFoundException, InvalidEnvironmentException,
            EnvironmentNotFoundException, ProductReleaseNotFoundException, ProductNotFoundException;
}
