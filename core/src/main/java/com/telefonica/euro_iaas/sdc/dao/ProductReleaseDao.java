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

import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductReleaseSearchCriteria;
import com.telefonica.fiware.commons.dao.BaseDAO;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;

/**
 * Defines the methods needed to persist ProductRelease objects.
 * 
 * @author Sergio Arroyo.
 */
public interface ProductReleaseDao extends BaseDAO<ProductRelease, Long> {
    /**
     * Find the product releases that match with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<ProductRelease> findByCriteria(ProductReleaseSearchCriteria criteria);

    /**
     * Find the product release that match with the given criteria.
     * 
     * @param product
     *            the product
     * @param version
     *            the release version
     * @return the element that match with the criteria.
     * @throws EntityNotFoundException
     *             if there is no product with the given version
     */
    ProductRelease load(Product product, String version) throws EntityNotFoundException;

}
