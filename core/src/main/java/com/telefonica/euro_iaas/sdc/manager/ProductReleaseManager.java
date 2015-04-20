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

import java.io.File;
import java.util.List;

import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductReleaseSearchCriteria;

/**
 * @author jesus.movilla
 *
 */
public interface ProductReleaseManager {

    /**
     * Retrieve a Product release for a given product and version.
     * 
     * @param product
     *            the product
     * @param version
     *            the version
     * @return the product release that match with the criteria
     * @throws EntityNotFoundException
     *             if the product release does not exists
     */
    ProductRelease load(Product product, String version) throws EntityNotFoundException;

    /**
     * Find all product releases that match with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the products releases.
     */
    List<ProductRelease> findReleasesByCriteria(ProductReleaseSearchCriteria criteria);

    /**
     * Insert the Product Release consisting on.
     * 
     * @param productRelase
     *            Object with name, version, transitableUploads..
     * @param recipes
     *            recipes to be uploaded to the chef server in a tar file
     * @param installable
     *            scripts/packages required to install/uninstall/configure the product
     * @throws AlreadyExistsProductReleaseException
     * @return the products releases.
     */
    ProductRelease insert(ProductRelease productRelase, File recipes, File installable, String token)
        throws AlreadyExistsProductReleaseException, InvalidProductReleaseException;

    /**
     * Delete the Product Release consisting on.
    * 
    * @param productRelease
    *            Object with name, version, transitableUploads..
    * @throws ProductReleaseNotFoundException
    *             thrown when try to delete a ProductRelease which is included
    *             in any ApplicationRelease object
    */
    void delete(ProductRelease productRelease) throws ProductReleaseNotFoundException,
        ProductReleaseStillInstalledException;
    
    /**
    * Update the Product Release (either the cookbook/installable).
    * 
    * @param productRelease
    *            Object with name, version, transitableUploads..
    * @param recipes
    *            recipes to be uploaded to the chef server in a tar file
    * @param installable
    *            scripts/packages required to install/uninstall/configure the product
    * @return productRelease
    * @throws InvalidProductReleaseException
    * @throws ProductReleaseNotFoundException
    */
    ProductRelease update(ProductRelease productRelease, File recipes, File installable, String token)
        throws ProductReleaseNotFoundException, InvalidProductReleaseException;

    /**
     * Insert the ProductRelease into the Database.
     * @param productRelease
     * @return
     * @throws AlreadyExistsProductReleaseException
     * @throws InvalidProductReleaseException
     */
    ProductRelease insert(ProductRelease productRelease) throws AlreadyExistsProductReleaseException,
        InvalidProductReleaseException;

}
