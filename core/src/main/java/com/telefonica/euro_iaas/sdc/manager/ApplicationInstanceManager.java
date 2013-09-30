package com.telefonica.euro_iaas.sdc.manager;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.AlreadyInstalledException;
import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
import com.telefonica.euro_iaas.sdc.exception.IncompatibleProductsException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.exception.NotInstalledProductsException;
import com.telefonica.euro_iaas.sdc.exception.NotTransitableException;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.EnvironmentInstance;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationInstanceSearchCriteria;

/**
 * Defines the operations the system shall be able to do with Applications
 * 
 * @author Sergio Arroyo, Jesus M. Movilla
 */
public interface ApplicationInstanceManager {

    /**
     * Install a list of products in a given vm.
     * 
     * @param vm
     *            the vm where instance will be running in
     * @param environmentInstance
     *            the environmentInstance where the application will be installed.
     * @param application
     *            the application to install.
     * @param configuration
     *            the configuration parameters
     * @return the installed application.
     * @throws NodeExecutionException
     *             if the installation fails in node
     * @throws IncompatibleProductsException
     *             if the selected products are not compatible with the given application
     * @throws AlreadyInstalledException
     *             if the application is running on the system
     * @throws NotInstalledProductsException
     *             if the needed products to install the application are not installed
     */
    ApplicationInstance install(VM vm, String vdc, EnvironmentInstance environmentInstance,
            ApplicationRelease application, List<Attribute> configuration) throws NodeExecutionException,
            IncompatibleProductsException, AlreadyInstalledException, NotInstalledProductsException;

    /**
     * Uninstall a previously installed application.
     * 
     * @param applicationInstance
     *            the candidate to uninstall
     * @throws NodeExecutionException
     *             if the action fails when the node is uninstalling.
     * @throws FSMViolationException
     *             if the transition from the previous state to the next is not possible
     */
    void uninstall(ApplicationInstance applicationInstance) throws NodeExecutionException, FSMViolationException;

    /**
     * Configure an installed product
     * 
     * @param applicationInstance
     *            the installed product to configure
     * @param configuration
     *            the configuration parameters.
     * @return the configured application.
     * @throws NodeExecutionException
     *             if the action fails when the node is configuring.
     * @throws FSMViolationException
     *             if the transition from the previous state to the next is not possible
     */
    ApplicationInstance configure(ApplicationInstance applicationInstance, List<Attribute> configuration)
            throws NodeExecutionException, FSMViolationException;

    /**
     * Upgrade an application instance form the actual version to newRelease.version.
     * 
     * @param applicationInstance
     *            the installed application
     * @param newRelease
     *            the new version of the application
     * @return the updated application.
     * @throws NodeExecutionException
     *             if can not upgrade the system in node.
     * @throws NotTransitableException
     *             if the transition can not be done
     * @throws IncompatibleProductsException
     *             if the selected products are not compatible with the new application's version
     * @throws FSMViolationException
     *             if the transition from the previous state to the next is not possible
     */
    ApplicationInstance upgrade(ApplicationInstance applicationInstance, ApplicationRelease newRelease)
            throws NodeExecutionException, NotTransitableException, IncompatibleProductsException,
            FSMViolationException;

    /**
     * Find the ApplicationInstance using the given id.
     * 
     * @param id
     *            the applicationInstance identifier
     * @return the applicationInstance
     * @throws EntityNotFoundException
     *             if the product instance does not exists
     */
    ApplicationInstance load(Long id) throws EntityNotFoundException;

    /**
     * Find the ApplicationInstance that match with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the applicationInstance
     * @throws EntityNotFoundException
     *             if the application instance does not exists
     * @throws NotUniqueResultException
     *             if there are more than a application that match with the given criteria
     */
    ApplicationInstance loadByCriteria(ApplicationInstanceSearchCriteria criteria) throws EntityNotFoundException,
            NotUniqueResultException;

    /**
     * Retrieve all ApplicationInstance created in the system.
     * 
     * @return the existent product instances.
     */
    List<ApplicationInstance> findAll();

    /**
     * Find the application instances that match with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<ApplicationInstance> findByCriteria(ApplicationInstanceSearchCriteria criteria);

}
