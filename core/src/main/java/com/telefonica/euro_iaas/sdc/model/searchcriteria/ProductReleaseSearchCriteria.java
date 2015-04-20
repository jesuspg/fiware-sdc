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

package com.telefonica.euro_iaas.sdc.model.searchcriteria;

import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.fiware.commons.dao.AbstractSearchCriteria;

/**
 * Provides some criteria to search ProductInstance entities.
 * 
 * @author Sergio Arroyo
 */
public class ProductReleaseSearchCriteria extends AbstractSearchCriteria {

    /**
     * The product.
     */
    private Product product;

    /**
     * The osType.
     */
    private String osType;

    /**
     * Default constructor
     */
    public ProductReleaseSearchCriteria() {
    }

    /**
     * @param page
     * @param pagesize
     * @param orderBy
     * @param orderType
     * @param vm
     * @param product
     */
    public ProductReleaseSearchCriteria(Integer page, Integer pageSize, String orderBy, String orderType,
            Product product) {
        super(page, pageSize, orderBy, orderType);
        this.product = product;
    }

    /**
     * @param orderBy
     * @param orderType
     * @param product
     */
    public ProductReleaseSearchCriteria(String orderBy, String orderType, Product product) {
        super(orderBy, orderType);
        this.product = product;
    }

    /**
     * @param page
     * @param pagesize
     * @param vm
     * @param product
     */
    public ProductReleaseSearchCriteria(Integer page, Integer pageSize, Product product) {
        super(page, pageSize);
        this.product = product;
    }

    /**
     * @param vm
     */
    public ProductReleaseSearchCriteria(Product product) {
        this.product = product;
    }

    /**
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @param product
     *            the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * @return the osType
     */
    public String getOSType() {
        return osType;
    }

    /**
     * @param osType
     *            the osTypeto set
     */
    public void setOSType(String osType) {
        this.osType = osType;
    }
}
