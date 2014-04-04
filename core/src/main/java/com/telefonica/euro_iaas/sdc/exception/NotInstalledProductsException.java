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

package com.telefonica.euro_iaas.sdc.exception;

import java.util.List;

import com.telefonica.euro_iaas.sdc.model.ProductRelease;

/**
 * Exception thrown when try to install an application and the needed products are not installed.
 * 
 * @author Sergio Arroyo
 */
@SuppressWarnings("serial")
public class NotInstalledProductsException extends Exception {

    private List<ProductRelease> products;

    /**
     * @param applications
     * @param product
     */
    public NotInstalledProductsException(List<ProductRelease> products) {
        this.products = products;

    }

    private String listProducts(List<ProductRelease> products) {
        String productList = "";
        for (ProductRelease product : products) {
            productList = productList.concat(", " + product.getProduct().getName() + "-" + product.getVersion());
        }
        return productList;
    }

    @Override
    public String getMessage() {
        return "These products are not installed " + listProducts(products);
    }

    /**
     * @return the products
     */
    public List<ProductRelease> getProducts() {
        return products;
    }

}
