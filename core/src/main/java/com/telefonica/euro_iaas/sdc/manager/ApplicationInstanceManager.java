package com.telefonica.euro_iaas.sdc.manager;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationInstanceSearchCriteria;

/**
 * Defines the operations the system shall be able to do with Applications
 *
 * @author Sergio Arroyo
 *
 */
public interface ApplicationInstanceManager {

    /**
     * Install a list of products in a given vm.
     *
     * @param vm
     *            the vm where instance will be running in
     * @param products
     *            the list of products where the application will be installed.
     * @param application
     *            the application to install.
     *
     * @return the installed application.
     */
    ApplicationInstance install(VM vm, List<ProductInstance> products,
            Application application);

    /**
     * Uninstall a previously installed application.
     *
     * @param applicationInstance
     *            the candidate to uninstall
     */
    void uninstall(ApplicationInstance applicationInstance);

    /**
     * Configure an installed product
     * @param applicationInstance the installed product to configure
     * @param configuration the configuration parameters.
     * @return the configured application.
     */
    ApplicationInstance configure(ApplicationInstance applicationInstance,
            List<Attribute> configuration);

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
    List<ApplicationInstance> findByCriteria(
            ApplicationInstanceSearchCriteria criteria);

}
