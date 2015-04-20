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

import com.telefonica.fiware.commons.dao.AlreadyExistsEntityException;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.fiware.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.dto.ProductAndReleaseDto;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductSearchCriteria;

/**
 * Defines the operations the system shall be able to do with Products.
 * 
 * @author Sergio Arroyo
 */
public interface ProductManager {

    /**
     * Insert the Product in SDC Database
     * 
     * @param product
     * @param string
     * @return
     * @throws AlreadyExistsEntityException
     */
    Product insert(Product product, String string) throws AlreadyExistsEntityException, InvalidEntityException;

    /**
     * Find the ProductInstance using the given id.
     * 
     * @param name
     *            the product identifier
     * @return the product
     * @throws EntityNotFoundException
     *             if the product instance does not exists
     */
    Product load(String name) throws EntityNotFoundException;

    /**
     * @param name
     * @return
     */
    boolean exist(String name);

    /**
     * Retrieve all Product created in the system.
     * 
     * @return the existent product instances.
     */
    List<Product> findAll();

    /**
     * Find the products that match with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<Product> findByCriteria(ProductSearchCriteria criteria);

    /**
     * Delete a certain product from SDC Database.
     * 
     * @param product
     */
    void delete(Product product);

    /**
     * Update a certain product from SDC Database.
     * 
     * @param product
     */
    void update(Product product);

    /**
     * Find the products and product release that match with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<ProductAndReleaseDto> findProductAndReleaseByCriteria(ProductSearchCriteria criteria);

    /**
     * Load metadata from product by productName and metadataName.
     * 
     * @param productName
     * @param metadataName
     * @return
     */
    Metadata loadMetadata(String productName, String metadataName) throws EntityNotFoundException;

    /**
     * Update metadata.
     * 
     * @param updatedMetadata
     */
    void updateMetadata(Metadata updatedMetadata);
}
