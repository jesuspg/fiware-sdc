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
    ProductRelease insert(ProductRelease productRelase, File recipes, File installable)
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
    ProductRelease update(ProductRelease productRelease, File recipes, File installable)
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
