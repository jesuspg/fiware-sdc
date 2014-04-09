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

package com.telefonica.euro_iaas.sdc.client.services;

import java.util.List;

import com.telefonica.euro_iaas.sdc.client.exception.InsertResourceException;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.Product;

/**
 * Provides the methods which encapsulate SDC's product management's related calls.
 * 
 * @author Sergio Arroyo
 */
public interface ProductService {

    /**
     * Add to the catalog the product.
     * 
     * @param product
     *            the release dto
     * @throws InsertResourceException
     * @return the created product.
     */
    Product add(Product product, String token, String tenant) throws InsertResourceException;

     /**
     * Delete the product from the catalogue.
     * 
     * @param pname
     *            product name
     * @return void.
     */
    void delete(String pname,  String token, String tenant);

    /**
     * Retrieve the product from the SDC catalog
     * 
     * @param pname
     *            the pname
     * @return the product 
     * @throws ResourceNotFoundException
     *             if the selected product does not exists.
     */
    Product load(String pname,  String token, String tenant) throws ResourceNotFoundException;

    /**
     * Retrieve all ProductReleases available in SDC catalog.
     * 
     * @param page
     *            for pagination is 0 based number(<i>nullable</i>)
     * @param pageSize
     *            for pagination, the number of items retrieved in a query (<i>nullable</i>)
     * @param orderBy
     *            the file to order the search (id by default <i>nullable</i>)
     * @param orderType
     *            defines if the order is ascending or descending (asc by default <i>nullable</i>)
     * @return the product that match with the criteria.
     */
    List<Product> findAll(Integer page, Integer pageSize, String orderBy, String orderType,  String token, String tenant);
    
    /**
     * Retrieve the attributes of a product.
     * @param pname
     *                 the product Name
     * @return list of attributes
     */
    List<Attribute> loadAttributes(String pname,  String token, String tenant) throws ResourceNotFoundException;
    
    /**
     * Retrieve the metadatas of a product.
     * @param pname
     *                 the product Name
     * @return list of metadatas
     */
    List<Metadata> loadMetadatas(String pname,  String token, String tenant)  throws ResourceNotFoundException;
}
