package com.telefonica.euro_iaas.sdc.manager.async;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationInstanceSearchCriteria;

/**
 * Defines the interface to work in asynchronous with Application Instances.
 * @author Sergio Arroyo
 *
 */
public interface ApplicationInstanceAsyncManager {

    /**
     * Install a list of products in a given vm.
     *
     * @param vm
     *            the vm where instance will be running in
     * @param vdc the vdc where the instance will be installed
     * @param products
     *            the list of products where the application will be installed.
     * @param application
     *            the application to install.
     * @param configuration the configuration parameters
     * @param task the task which contains the information about the async execution
     * @param callback if not empty, contains the url where the result of the
     * execution will be sent
     *
     */
    void install(VM vm, String vdc, List<ProductInstance> products,
            ApplicationRelease application, List<Attribute> configuration,
            Task task, String callback);


    /**
     * Configure an installed application
     * @param applicationInstance the installed application to configure
     * @param configuration the configuration parameters.
     * @param task the task which contains the information about the async execution
     * @param callback if not empty, contains the url where the result of the
     * execution will be sent
     */
    void configure(ApplicationInstance applicationInstance,
            List<Attribute> configuration, Task task, String callback);

    /**
     * Upgrade a ApplicationInstance
     * @param applicationInstance the installed application to upgrade
     * @param applicationRelease the applicationRelease to upgrade to.
     * @param task the task which contains the information about the async execution
     * @param callback if not empty, contains the url where the result of the
     * execution will be sent
     */
    void upgrade(ApplicationInstance applicationInstance,
            ApplicationRelease applicationRelease, Task task, String callback);

    /**
     * Uninstall a previously installed application.
     * @param applicationInstance the candidate to uninstall
     * @param task the task which contains the information about the async execution
     * @param callback if not empty, contains the url where the result of the
     * execution will be sent
     */
    void uninstall(ApplicationInstance applicationInstance, Task task, String callback);

    /**
     * Find the ApplicationInstance using the given id.
     * @param id the applicationInstance identifier
     * @return the applicationInstance
     * @throws EntityNotFoundException if the application instance does not exists
     */
    ApplicationInstance load(Long id) throws EntityNotFoundException;


    /**
     * Find the ApplicationInstance that match with the given criteria.
     * @param criteria the search criteria
     * @return the applicationInstance
     * @throws EntityNotFoundException if the application instance does not exists
     * @throws NotUniqueResultException if there are more than a application that
     * match with the given criteria
     */
    ApplicationInstance loadByCriteria(ApplicationInstanceSearchCriteria criteria)
        throws EntityNotFoundException, NotUniqueResultException;

    /**
     * Find the application instances that match with the given criteria.
     * @param criteria the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<ApplicationInstance> findByCriteria(ApplicationInstanceSearchCriteria criteria);


}
