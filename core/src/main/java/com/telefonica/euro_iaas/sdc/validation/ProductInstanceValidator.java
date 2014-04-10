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

package com.telefonica.euro_iaas.sdc.validation;

import com.telefonica.euro_iaas.sdc.exception.AlreadyInstalledException;
import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
import com.telefonica.euro_iaas.sdc.exception.NotTransitableException;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;

/**
 * Define all possible validations for product instances
 * 
 * @author Sergio Arroyo
 */
public interface ProductInstanceValidator {

    /**
     * Verifies the product could be installed
     * 
     * @param product
     *            the product
     * @throws AlreadyInstalledException
     */
    void validateInstall(ProductInstance product) throws AlreadyInstalledException;

    /**
     * Verify if the given product could be uninstalled
     * 
     * @param product
     *            the product
     * @throws ApplicationInstalledException
     *             if there is some applications installed on the product
     * @throws FSMViolationException
     *             if it can not be uninstalled due to previous status
     */
    void validateUninstall(ProductInstance product) throws FSMViolationException;

    /**
     * Verify if the given product could be configured
     * 
     * @param product
     *            the product
     * @throws FSMViolationException
     *             if it can not be configured due to previous status
     */
    void validateConfigure(ProductInstance product) throws FSMViolationException;

    /**
     * Verify if the given product could have acs deployed
     * 
     * @param product
     *            the product
     * @throws FSMViolationException
     *             if it can not be configured due to previous status
     */
    void validateDeployArtifact(ProductInstance product) throws FSMViolationException;

    /**
     * Verify if the given product could be upgraded to the selected version
     * 
     * @param product
     *            the product
     * @param newRelease
     *            the new version
     * @throws FSMViolationException
     * @throws NotTransitableException
     * @throws ApplicationIncompatibleException
     */
    void validateUpdate(ProductInstance product, ProductRelease newRelease) throws FSMViolationException,
            NotTransitableException;

}
