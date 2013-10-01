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

package com.telefonica.euro_iaas.sdc.manager.async;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;

/**
 * Defines the interface to work with async requests.
 * 
 * @author Sergio Arroyo
 */
public interface ProductInstanceAsyncManager {

    /**
     * Install a list of products in a given vm.
     * 
     * @param vm
     *            the vm where instance will be running in
     * @param vdc
     *            the vdc where the instance will be installed
     * @param product
     *            the product to install
     * @param attributes
     *            the configuration
     * @param task
     *            the task which contains the information about the async execution
     * @param callback
     *            if not empty, contains the url where the result of the execution will be sent
     */
    void install(VM vm, String vdc, ProductRelease product, List<Attribute> attributes, Task task, String callback);

    /**
     * Configure an installed product
     * 
     * @param productInstance
     *            the installed product to configure
     * @param configuration
     *            the configuration parameters.
     * @param task
     *            the task which contains the information about the async execution
     * @param callback
     *            if not empty, contains the url where the result of the
     */
    void configure(ProductInstance productInstance, List<Attribute> configuration, Task task, String callback);

    /**
     * Upgrade a ProductInstance
     * 
     * @param productInstance
     *            the installed product to upgrade
     * @param productRelease
     *            the productRelease to upgrade to.
     * @param task
     *            the task which contains the information about the async execution
     * @param callback
     *            if not empty, contains the url where the result of the execution will be sent
     */
    void upgrade(ProductInstance productInstance, ProductRelease productRelease, Task task, String callback);

    /**
     * Uninstall a previously installed product.
     * 
     * @param productInstance
     *            the candidate to uninstall
     * @param task
     *            the task which contains the information about the async execution
     * @param callback
     *            if not empty, contains the url where the result of the execution will be sent
     */
    void uninstall(ProductInstance productInstance, Task task, String callback);

    /**
     * Find the ProductInstance using the given id.
     * 
     * @param vdc
     *            the vdc
     * @param id
     *            the productInstance identifier
     * @return the productInstance
     * @throws EntityNotFoundException
     *             if the product instance does not exists
     */
    ProductInstance load(String vdc, Long id) throws EntityNotFoundException;

    ProductInstance load(String vdc, String name) throws EntityNotFoundException;

    /**
     * Find the ProductInstance that match with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the productInstance
     * @throws EntityNotFoundException
     *             if the product instance does not exists
     * @throws NotUniqueResultException
     *             if there are more than a product that match with the given criteria
     */
    ProductInstance loadByCriteria(ProductInstanceSearchCriteria criteria) throws EntityNotFoundException,
            NotUniqueResultException;

    /**
     * Find the product instances that match with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<ProductInstance> findByCriteria(ProductInstanceSearchCriteria criteria);

}
