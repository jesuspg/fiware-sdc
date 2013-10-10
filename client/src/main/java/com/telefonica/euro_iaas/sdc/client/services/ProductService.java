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
    Product add(Product product) throws InsertResourceException;

     /**
     * Delete the product from the catalogue.
     * 
     * @param pname
     *            product name
     * @return void.
     */
    void delete(String pname);

    /**
     * Retrieve the product from the SDC catalog
     * 
     * @param pname
     *            the pname
     * @return the product 
     * @throws ResourceNotFoundException
     *             if the selected product does not exists.
     */
    Product load(String pname) throws ResourceNotFoundException;

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
    List<Product> findAll(Integer page, Integer pageSize, String orderBy, String orderType);
    
    /**
     * Retrieve the attributes of a product.
     * @param pname
     *                 the product Name
     * @return list of attributes
     */
    List<Attribute> loadAttributes(String pname) throws ResourceNotFoundException;
    
    /**
     * Retrieve the metadatas of a product.
     * @param pname
     *                 the product Name
     * @return list of metadatas
     */
    List<Metadata> loadMetadatas(String pname)  throws ResourceNotFoundException;
}
