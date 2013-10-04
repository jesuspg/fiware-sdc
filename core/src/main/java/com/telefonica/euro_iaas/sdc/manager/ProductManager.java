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

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;

import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductSearchCriteria;

/**
 * Defines the operations the system shall be able to do with Products.
 * 
 * @author Sergio Arroyo
 */
public interface ProductManager {

    /**
     * Insert the Product in SDC Database
     * @param product
     * @return
     * @throws AlreadyExistsEntityException
     */
    Product insert(Product product) throws AlreadyExistsEntityException, InvalidEntityException;
    
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
}
