/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.sdc.manager;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.exception.AlreadyInstalledException;
import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
import com.telefonica.euro_iaas.sdc.exception.InstallatorException;
import com.telefonica.euro_iaas.sdc.exception.InvalidInstallProductRequestException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.exception.NotTransitableException;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;

/**
 * Defines the operations the system shall be able to do with Products.
 * 
 * @author Sergio Arroyo
 */
public interface ProductInstanceManager {

    public String chefInstallator = "chef";
    public String puppetInstallator = "puppet";

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
     * @return the of installed product.
     */
    ProductInstance install(VM vm, String vdc, ProductRelease product, List<Attribute> attributes)
            throws NodeExecutionException, AlreadyInstalledException, InvalidInstallProductRequestException, EntityNotFoundException;

    /**
     * Uninstall a previously installed product.
     * 
     * @param productInstance
     *            the candidate to uninstall
     * @throws NodeExecutionException
     *             if any error happen during the uninstallation in node
     * @throws FSMViolationException
     *             if try to make a forbidden transition
     */
    void uninstall(ProductInstance productInstance) throws NodeExecutionException, FSMViolationException, EntityNotFoundException;

    /**
     * Updates a product instance and persist it in the database.
     * 
     * @param productInstance
     *            the product instance to modify
     * @return the product instance updated
     * @throws InvalidEntityException
     */
    ProductInstance update(ProductInstance productInstance) throws InvalidEntityException;

    /**
     * Configure an installed product.
     * 
     * @param productInstance
     *            the installed product to configure
     * @param configuration
     *            the configuration parameters.
     * @return the configured product.
     * @throws FSMViolationException
     *             if try to make a forbidden transition
     * @throws InstallatorException
     */
    ProductInstance configure(ProductInstance productInstance, List<Attribute> configuration)
            throws NodeExecutionException, FSMViolationException, InstallatorException;

    /**
     * Upgrade a ProductInstance.
     * 
     * @param productInstance
     *            the installed product to upgrade
     * @param productRelease
     *            the productRelease to upgrade to.
     * @return the productInstance upgraded.
     * @throws NotTransitableException
     *             if the selected version is not compatible with the installed
     *             product
     * @throws NodeExecutionException
     *             if any error happen during the upgrade in node
     * @throws FSMViolationException
     *             if try to make a forbidden transition
     * @throws InstallatorException
     */
    ProductInstance upgrade(ProductInstance productInstance, ProductRelease productRelease)
            throws NotTransitableException, NodeExecutionException, FSMViolationException, InstallatorException, EntityNotFoundException;

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

    /**
     * Find the ProductInstance by name.
     * 
     * @param vdc
     *            the virtual data center
     * @param name
     *            the product instance name
     * @return the product instance
     * @throws EntityNotFoundException
     */
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
     *             if there are more than a product that match with the given
     *             criteria
     */
    ProductInstance loadByCriteria(ProductInstanceSearchCriteria criteria) throws EntityNotFoundException,
            NotUniqueResultException;

    /**
     * Retrieve all ProductInstance created in the system.
     * 
     * @return the existent product instances.
     */
    List<ProductInstance> findAll();

    /**
     * Find the product instances that match with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<ProductInstance> findByCriteria(ProductInstanceSearchCriteria criteria);

}
