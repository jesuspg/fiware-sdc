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

import com.telefonica.euro_iaas.sdc.model.ProductInstance;

/**
 * Exception thrown when try to delete a ProductRelease which is installed on some VMs
 * 
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class ProductReleaseStillInstalledException extends Exception {

    private List<ProductInstance> productInstances;

    public ProductReleaseStillInstalledException() {
        super();
    }

    public ProductReleaseStillInstalledException(List<ProductInstance> productInstances) {
        this.productInstances = productInstances;
    }

    public ProductReleaseStillInstalledException(String msg) {
        super(msg);
    }

    public ProductReleaseStillInstalledException(Throwable e) {
        super(e);
    }

    public ProductReleaseStillInstalledException(String msg, Throwable e) {
        super(msg, e);
    }

    /**
     * @return the productInstances
     */
    public List<ProductInstance> getProductInstances() {
        return productInstances;
    }

    /**
     * @param productInstances
     *            the productInstances to set
     */
    public void setProductInstances(List<ProductInstance> productInstances) {
        this.productInstances = productInstances;
    }

}
