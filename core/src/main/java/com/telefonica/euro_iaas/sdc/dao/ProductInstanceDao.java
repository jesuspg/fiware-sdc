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

package com.telefonica.euro_iaas.sdc.dao;

import java.util.List;

import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;
import com.telefonica.fiware.commons.dao.BaseDAO;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;

/**
 * Defines the methods needed to persist AppState objects.
 * 
 * @author Sergio Arroyo.
 */
public interface ProductInstanceDao extends BaseDAO<ProductInstance, Long> {
    /**
     * Find the product instances that match with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<ProductInstance> findByCriteria(ProductInstanceSearchCriteria criteria);

    /**
     * Find the unique product instances that match with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the element that match with the criteria.
     * @throws NotUniqueResultException
     *             if the number of elements that match with the criteria is different to one.
     */
    ProductInstance findUniqueByCriteria(ProductInstanceSearchCriteria criteria) throws NotUniqueResultException;

    ProductInstance load(String name) throws EntityNotFoundException;

    ProductInstance findByProductInstanceName(String productInstanceName) throws EntityNotFoundException;

    /**
     * Find lst of product instances by hostname where they are installed
     * 
     * @param hostname
     * @return
     */
    List<ProductInstance> findByHostname(String hostname) throws EntityNotFoundException;
}
